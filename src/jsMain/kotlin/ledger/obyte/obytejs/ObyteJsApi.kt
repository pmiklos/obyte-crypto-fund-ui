package ledger.obyte.obytejs

import kotlinx.coroutines.await
import ledger.obyte.AddressDefinition
import ledger.obyte.AddressDefinitionService
import ledger.obyte.AssetMetadata
import ledger.obyte.AssetMetadataService
import ledger.obyte.AutonomousAgentService
import ledger.obyte.BalanceService
import ledger.obyte.BaseAgentService
import ledger.obyte.ConfigurationService
import ledger.obyte.ObyteApi
import ledger.obyte.SubAgent
import ledger.obyte.mock.MockAddressDefinitionService
import ledger.obyte.mock.MockAutonomousAgentService
import ledger.obyte.mock.MockBalanceService

class ObyteJsApi(
    obyte: Client,
) : ObyteApi,
    AddressDefinitionService by MockAddressDefinitionService,
    AssetMetadataService by ObyteJsAssetMetadataService(obyte),
    AutonomousAgentService by MockAutonomousAgentService,
    BalanceService by MockBalanceService,
    BaseAgentService by ObyteJsBaseAgentService(obyte),
    ConfigurationService by ObyteJsConfigurationService(obyte)

val Testnet by lazy {
    Client("wss://obyte.org/bb-test", mapOf("testnet" to true))
}

class ObyteJsConfigurationService(client: Client) : ConfigurationService {

    override val network = if (client.options["testnet"] as Boolean) "testnet" else "livenet"
    override val node = client.client.address
    override fun explorerUrl(unitOrAddress: String) = "https://testnetexplorer.obyte.org/#${unitOrAddress}"

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
    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata {
        val tokenRegistry = client.api.getOfficialTokenRegistryAddress() // tokens.ooo
        val symbol = client.api.getSymbolByAsset(tokenRegistry, assetHash).await()
        val decimals = client.api.getDecimalsBySymbolOrAsset(tokenRegistry, assetHash).await()

        return AssetMetadata(
            ticker = symbol,
            decimals = decimals
        )
    }

}

private fun GetAasByBaseAasRequest(): GetAasByBaseAasRequest = js("{}")