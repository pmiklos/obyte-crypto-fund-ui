package funddetails.usecase

import funddetails.domain.AssetAllocation
import funddetails.domain.Balance
import funddetails.domain.div

object CalculateAssetPaymentUseCase {

    operator fun invoke(
        allocation: List<AssetAllocation>,
        totalShares: Balance,
        sharesToBuy: Double
    ): List<Balance> {
        if (totalShares.amount == 0L) {
            return allocation.map {
                it.balance.copy(
                    amount = (it.targetPercentage * sharesToBuy).toLong()
                )
            }
        }
        val percentage = sharesToBuy / totalShares
        return allocation.map { it.balance * percentage }
    }

}