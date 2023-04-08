package ledger.obyte.obytejs

import kotlin.js.Promise
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.await
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import ledger.obyte.AddressDefinition
import ledger.obyte.AddressDefinitionService
import ledger.obyte.AssetMetadata
import ledger.obyte.AssetMetadataService
import ledger.obyte.AutonomousAgentService
import ledger.obyte.Balance
import ledger.obyte.BalanceService
import ledger.obyte.BaseAgentService
import ledger.obyte.ConfigurationService
import ledger.obyte.ObyteApi
import ledger.obyte.SubAgent
import ledger.obyte.ValidationService

class ObyteJsApi(
    obyte: Client,
) : ObyteApi,
    AddressDefinitionService by ObyteJsAddressDefinitionService(obyte),
    AssetMetadataService by ObyteJsAssetMetadataService(obyte),
    AutonomousAgentService by ObyteJsAutonomousAgentService(obyte),
    BalanceService by ObyteJsBalanceService(obyte),
    BaseAgentService by ObyteJsBaseAgentService(obyte),
    ConfigurationService by ObyteJsConfigurationService(obyte),
    ValidationService by ObyteJsValidationService

val Testnet by lazy {
    Client(
        "wss://obyte.org/bb-test", mapOf(
            "testnet" to true
        )
    )
}

private class ObyteJsAddressDefinitionService(private val client: Client) : AddressDefinitionService {
    override suspend fun getDefinitionForAddress(address: String): AddressDefinition {
        val response = client.withRetry {
            api.getDefinition(address).await(7.seconds)
        }

        return AddressDefinition(
            type = response[0] as String,
            params = response[1].unsafeCast<Map<String, Any>>()
        )
    }
}

private class ObyteJsAutonomousAgentService(private val client: Client) : AutonomousAgentService {
    override suspend fun getState(address: String): Map<String, Any?> {
        console.log("In getState", currentCoroutineContext())
        try {
            val vars = client.withRetry {
                api.getAaStateVars(GetAaStateVarsRequest().apply {
                    this.address = address
                }).await(7.seconds)
            }
            return jsObjectToMap(vars)
        } finally {
            console.log("After getAaStateVars", currentCoroutineContext())
        }
    }
}

private class ObyteJsBalanceService(private val client: Client) : BalanceService {
    override suspend fun getBalances(addresses: List<String>): Map<String, Map<String, Balance>> {
        val balances = client.withRetry {
            api.getBalances(addresses.toTypedArray()).await(7.seconds)
        }

        return (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
            .invoke(balances)
            .associate { (address, balances) -> address as String to balances }
            .mapValues { (_, balances) ->
                (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
                    .invoke(balances.asDynamic())
                    .associate { (asset, balance) -> asset as String to balance.unsafeCast<BalanceResponse>() }
                    .mapValues { (_, balance) ->
                        Balance(
                            stable = balance.stable.toLong(),
                            pending = balance.pending.toLong()
                        )
                    }
            }
    }

}

private class ObyteJsConfigurationService(client: Client) : ConfigurationService {

    override val network = if (client.options["testnet"] as Boolean) "testnet" else "livenet"
    override val node = client.client.address
    override fun explorerUrl(unitOrAddress: String) = when (network) {
        "testnet" -> "https://testnetexplorer.obyte.org/#${unitOrAddress}"
        "livenet" -> "https://explorer.obyte.org/#${unitOrAddress}"
        else -> "#"
    }
}

private class ObyteJsBaseAgentService(private val client: Client) : BaseAgentService {

    override suspend fun getSubAgents(baseAgent: String): List<SubAgent> {
        val subAgents = client.withRetry {
            api.getAasByBaseAas(GetAasByBaseAasRequest().apply {
                base_aa = baseAgent
            }).await(7.seconds)
        }

        return subAgents.map {
            SubAgent(
                address = it.address,
                definition = AddressDefinition(
                    type = it.definition[0] as String,
                    params = it.definition[1].unsafeCast<Map<String, Any>>()
                )
            )
        }.toList()
    }
}

private class ObyteJsAssetMetadataService(private val client: Client) : AssetMetadataService {

    private val registries = AssetRegistries(client)

    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata = client.withRetry {
        val coroutineContext = currentCoroutineContext()
        // using messy Promise api because .await() did not throw proper exception on errors eg. no asset metadata
        api.getAssetMetadata(assetHash)
            .then { registryUnit ->
                api.getJoint(registryUnit.metadata_unit)
                    .then { metadataJoint ->
                        val registry = registries[registryUnit.registry_address]
                        Pair(registry, metadataJoint.joint.unit)
                    }
            }.then { (registry, metadataUnit) ->
                Promise { resolve, _ ->
                    CoroutineScope(coroutineContext).launch {
                        val assetMetadata = registry.getAssetMetadata(metadataUnit)
                        resolve(assetMetadata)
                    }
                }
            }
            .catch {
                console.error("ERROR: getting asset metadata for $assetHash: $it")
                AssetMetadata(
                    ticker = assetHash.substring(0, 6),
                    decimals = 0,
                    description = ""
                )
            }
            .await(7.seconds)
    }
}

private object ObyteJsValidationService : ValidationService {
    override fun validateAddress(address: String): ValidationService.ValidateAddressResult = when {
        isValidAddress(address) -> ValidationService.ValidateAddressResult.Valid
        address.length != 32 -> ValidationService.ValidateAddressResult.Invalid("Address must be 32 characters")
        else -> ValidationService.ValidateAddressResult.Invalid("Not a valid Obyte address")
    }
}

private suspend fun <T> Client.withRetry(timingOutBlock: suspend Client.() -> T): T {
    repeat(3) {
        try {
            return timingOutBlock(this)
        } catch (e: TimeoutCancellationException) {
            client.close()
            client.connect()
        }
    }
    throw RuntimeException("Timed out, no more retries")
}

private suspend fun <T> Promise<T>.await(timeout: Duration): T = withTimeout(timeout) {
    await()
}
