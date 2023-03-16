package funddetails.domain

/**
 * @param balance the current asset balance of this allocation slot.
 * @param targetPercentage is the allocation percentage calculated in minor currency units
 */
data class AssetAllocation(
    val balance: Balance,
    val targetPercentage: Double
)