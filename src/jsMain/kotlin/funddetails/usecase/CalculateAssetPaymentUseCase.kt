package funddetails.usecase

import funddetails.domain.AssetAllocation
import funddetails.domain.Balance
import kotlin.math.round

object CalculateAssetPaymentUseCase {

    operator fun invoke(
        allocation: List<AssetAllocation>,
        sharesToBuy: Double
    ): List<Balance> {
        return allocation.map {
            it.balance.copy(
                amount = round(it.targetPercentage * sharesToBuy).toLong()
            )
        }
    }

}