package obyte

import kotlinx.coroutines.delay

class MockAssetMetadataService : AssetMetadataService {
    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata {
        delay(500)
        return when (assetHash) {
            "fSwaCprr3OSNHXTLDtOK3lflKEKvQi7ypWQSh1FfK1E=" -> AssetMetadata(
                ticker = "BTC",
                decimals = 8
            )

            "3aw7r8dm0C/TG3w2CQGyCc8ukbMpeHJ/SXgbej/WXz8=" -> AssetMetadata(
                ticker = "ETH",
                decimals = 6
            )

            else -> throw RuntimeException("No such asset")
        }
    }
}

class MockBaseAgentService : BaseAgentService {
    override suspend fun getSubAgents(baseAgent: String): List<String> {
        delay(1000L)
        return listOf("FUND0000000000000000000000000001", "FUND0000000000000000000000000002")
    }
}

class MockAddressDefinitionService : AddressDefinitionService {
    override suspend fun getDefinitionForAddress(address: String): AddressDefinition =
        when (address) {
            "FUND0000000000000000000000000001" -> AddressDefinition(
                type = "autonomous agent",
                params = mapOf(
                    "portfolio" to listOf(
                        mapOf(
                            "asset" to "fSwaCprr3OSNHXTLDtOK3lflKEKvQi7ypWQSh1FfK1E=",
                            "percentage" to 0.95
                        ),
                        mapOf(
                            "asset" to "3aw7r8dm0C/TG3w2CQGyCc8ukbMpeHJ/SXgbej/WXz8=",
                            "percentage" to 0.05
                        )
                    )
                )
            )

            "FUND0000000000000000000000000002" -> AddressDefinition(
                type = "autonomous agent",
                params = mapOf(
                    "portfolio" to listOf(
                        mapOf(
                            "asset" to "fSwaCprr3OSNHXTLDtOK3lflKEKvQi7ypWQSh1FfK1E=",
                            "percentage" to 0.30
                        ),
                        mapOf(
                            "asset" to "3aw7r8dm0C/TG3w2CQGyCc8ukbMpeHJ/SXgbej/WXz8=",
                            "percentage" to 0.70
                        )
                    )
                )
            )

            else -> throw RuntimeException("No such address")
        }
}

