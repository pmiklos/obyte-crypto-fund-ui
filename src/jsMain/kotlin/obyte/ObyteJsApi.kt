package obyte

import kotlinx.coroutines.await

class ObyteJsApi(
    obyte: Client,
) : ObyteApi,
    AddressDefinitionService by MockAddressDefinitionService,
    AssetMetadataService by ObyteJsAssetMetadataService(obyte),
    AutonomousAgentService by MockAutonomousAgentService,
    BalanceService by MockBalanceService,
    BaseAgentService by ObyteJsBaseAgentService(obyte)

val Testnet by lazy {
    Client("wss://obyte.org/bb-test", mapOf("testnet" to true))
}

class ObyteJsBaseAgentService(private val client: Client): BaseAgentService {

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

class ObyteJsAssetMetadataService(private val client: Client): AssetMetadataService {
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