package funddetails.domain

import common.movePointLeft

data class Balance(
    val asset: Asset,
    val amount: Long
) {
    operator fun times(other: Double) = copy(amount = (amount * other).toLong())

    fun toDouble(): Double = amount.toDouble().movePointLeft(asset.decimals)
}

operator fun Double.div(balance: Balance) = this / balance.toDouble()