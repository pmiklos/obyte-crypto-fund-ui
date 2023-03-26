package ledger.obyte.obytejs

import kotlinx.coroutines.await
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

class ObyteJsApi(
    obyte: Client,
) : ObyteApi,
    AddressDefinitionService by ObyteJsAddressDefinitionService(obyte),
    AssetMetadataService by ObyteJsAssetMetadataService(obyte),
    AutonomousAgentService by ObyteJsAutonomousAgentService(obyte),
    BalanceService by ObyteJsBalanceService(obyte),
    BaseAgentService by ObyteJsBaseAgentService(obyte),
    ConfigurationService by ObyteJsConfigurationService(obyte)

val Testnet by lazy {
    Client("wss://obyte.org/bb-test", mapOf("testnet" to true))
}

class ObyteJsAddressDefinitionService(private val client: Client) : AddressDefinitionService {
    override suspend fun getDefinitionForAddress(address: String): AddressDefinition {
        val response = client.api.getDefinition(address).await()

        return AddressDefinition(
            type = response[0] as String,
            params = response[1].unsafeCast<Map<String, Any>>()
        )
    }
}

class ObyteJsAutonomousAgentService(private val client: Client) : AutonomousAgentService {
    override suspend fun getState(address: String): Map<String, Any?> {
        val vars = client.api.getAaStateVars(GetAaStateVarsRequest().apply {
            this.address = address
        }).await()

        return jsObjectToMap(vars)
    }
}

class ObyteJsBalanceService(private val client: Client) : BalanceService {
    override suspend fun getBalances(addresses: List<String>): Map<String, Map<String, Balance>> {
        val balances = client.api.getBalances(addresses.toTypedArray()).await()

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

class ObyteJsConfigurationService(client: Client) : ConfigurationService {

    override val network = if (client.options["testnet"] as Boolean) "testnet" else "livenet"
    override val node = client.client.address
    override fun explorerUrl(unitOrAddress: String) = when (network) {
        "testnet" -> "https://testnetexplorer.obyte.org/#${unitOrAddress}"
        "livenet" -> "https://explorer.obyte.org/#${unitOrAddress}"
        else -> "#"
    }
}

class ObyteJsBaseAgentService(private val client: Client) : BaseAgentService {

    override suspend fun getSubAgents(baseAgent: String): List<SubAgent> {
        val subAgents = client.api.getAasByBaseAas(GetAasByBaseAasRequest().apply {
            base_aa = baseAgent
        }).await()

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

class ObyteJsAssetMetadataService(private val client: Client) : AssetMetadataService {

    private val registries = AssetRegistries(client)

    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata {
        val registryUnit = client.api.getAssetMetadata(assetHash).await()
        val metadataJoint = client.api.getJoint(registryUnit.metadata_unit).await()
        val registry = registries[registryUnit.registry_address]

        return try {
            registry.getAssetMetadata(metadataJoint.joint.unit)
        } catch (e: Exception) {
            console.log(e)
            AssetMetadata(
                ticker = assetHash,
                decimals = 0,
                description = ""
            )
        }
    }
}