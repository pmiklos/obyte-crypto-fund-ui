package funddetails.view

import funddetails.domain.Asset
import funddetails.domain.AssetAllocation
import funddetails.domain.Balance
import funddetails.view.component.AssetPaymentTableBean

data class TradingBean(
    val fundAddress: String = "",
    val sharesToBuy: String = "",
    val sharesToRedeem: String = "",
    val shareSymbol: String = "",
    val allocation: List<AssetAllocation> = emptyList(),
    val totalShares: Balance = Balance(Asset("", "", 0), 0),
    val assetPaymentTable: AssetPaymentTableBean = AssetPaymentTableBean(emptyList()),
    val assetRedemptionTable: AssetPaymentTableBean = AssetPaymentTableBean(emptyList()),
    val assetPaymentUrl: String = "",
    val assetRedemptionUrl: String = "",
    val walletAddress: String = ""
) {

    val invalidPurchaseAmount = sharesToBuy.toDoubleOrNull()?.run {
        this <= 0.0
    } ?: false

    val sharesBuyable = sharesToBuy.toDoubleOrNull()?.run {
        this > 0.0 && walletAddress.isNotBlank()
    } ?: false

    val invalidRedemptionAmount = sharesToRedeem.toDoubleOrNull()?.run {
        this <= 0.0 || this > totalShares.toDouble()
    } ?: false

    val sharesRedeemable = sharesToRedeem.toDoubleOrNull()?.run {
        this > 0.0 && this <= totalShares.toDouble() && walletAddress.isNotBlank()
    } ?: false

    val missingWallet = walletAddress.isBlank()

}