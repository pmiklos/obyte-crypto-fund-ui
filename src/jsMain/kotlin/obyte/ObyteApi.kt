package obyte

data class AssetMetadata(val ticker: String, val decimals: Int)
data class AddressDefinition(val type: String, val params: Map<String, Any>)
data class Balance(val stable: Long, val pending: Long)

interface AssetMetadataService {
    suspend fun getAssetMetadata(assetHash: String): AssetMetadata
}

interface AddressDefinitionService {
    suspend fun getDefinitionForAddress(address: String): AddressDefinition
}

interface BaseAgentService {
    suspend fun getSubAgents(baseAgent: String): List<String>
}

interface BalanceService {
    suspend fun getBalances(addresses: List<String>): Map<String, Map<String, Balance>>
}