package funddetails

import common.movePointLeft

data class FundDetails(
    val address: String,
    val totalShares: Balance,
    val allocation: List<AssetAllocation>
) {

    private val totalNormalizedAllocation: Double = allocation.fold(0.0) { sum, allocation ->
        sum + allocation.normalizedAllocation
    }

    /**
     * The real allocation percentage calculated from asset allocations in major currency units.
     */
    val AssetAllocation.adjustedPercentage: Double
        get() {
            return normalizedAllocation / totalNormalizedAllocation
        }
}

data class Asset(
    val hash: String,
    val name: String,
    val decimals: Int
)

data class Balance(
    val asset: Asset,
    val amount: Long
)

/**
 * @param balance the current asset balance of this allocation slot.
 * @param targetPercentage is the allocation percentage calculated in minor currency units
 */
data class AssetAllocation(
    val balance: Balance,
    val targetPercentage: Double
) {
    /**
     * The amount of allocated coins in major currency unit.
     */
    val normalizedAllocation = targetPercentage.movePointLeft(balance.asset.decimals)
}
