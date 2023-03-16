package funddetails.domain

import common.movePointLeft

data class FundDetails(
    val address: String,
    val totalShares: Balance,
    val allocation: List<AssetAllocation>
) {

    /**
     * The real allocation percentage calculated from asset allocations in major currency units.
     */
    val AssetAllocation.adjustedPercentage: Double get() = normalizedAllocation / totalNormalizedAllocation

    /**
     * The amount of allocated coins in major currency unit.
     */
    private val AssetAllocation.normalizedAllocation get() = targetPercentage.movePointLeft(balance.asset.decimals)

    private val totalNormalizedAllocation: Double = allocation.fold(0.0) { sum, allocation ->
        sum + allocation.normalizedAllocation
    }

}