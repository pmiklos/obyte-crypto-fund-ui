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
    /**
     * <pre>
     *     {
     *          TMWNLXR42CKIP4A774BQGNVBZAPHY7GH: {
     *              base: {
     *                  stable: 838,
     *                  pending: 0
     *                  },
     *              's+bzDkwx0TVMtdyf9YU4wEA23oInOUzulO+r5WxBUZs=': {
     *                  stable: 98,
     *                  pending: 0
     *              }
     *          }
     *     }
     * </pre>
     */
    suspend fun getBalances(addresses: List<String>): Map<String, Map<String, Balance>>
}

interface AutonomousAgentService {
    suspend fun getState(address: String): Map<String, String>
}