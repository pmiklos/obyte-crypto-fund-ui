package obyte

data class AssetMetadata(val ticker: String, val decimals: Int)
data class AddressDefinition(val type: String, val params: Map<String, Any>)

interface AssetMetadataService {
    suspend fun getAssetMetadata(assetHash: String): AssetMetadata
}

interface AddressDefinitionService {
    suspend fun getDefinitionForAddress(address: String): AddressDefinition
}

interface BaseAgentService {
    suspend fun getSubAgents(baseAgent: String): List<String>
}
