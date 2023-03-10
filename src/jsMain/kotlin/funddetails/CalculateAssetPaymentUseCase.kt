package funddetails

object CalculateAssetPaymentUseCase {

    operator fun invoke(
        allocation: List<AssetAllocation>,
        totalShares: Balance,
        sharesToBuy: Double
    ): List<Balance> {
        val percentage = sharesToBuy / totalShares
        return allocation.map { it.balance * percentage }
    }

}