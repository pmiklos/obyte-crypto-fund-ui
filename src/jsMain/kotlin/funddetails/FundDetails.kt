package funddetails

data class FundDetails(
    val address: String,
    val totalShares: Balance,
    val allocation: List<AssetAllocation>
)

data class Asset(
    val hash: String,
    val name: String,
    val decimals: Int
)

data class Balance(
    val asset: Asset,
    val amount: Long
)

data class AssetAllocation(
    val balance: Balance,
    val targetPercentage: Double
)
