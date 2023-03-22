package funddetails.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import common.Resource
import funddetails.domain.Balance
import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.GetFundDetailsUseCase
import funddetails.view.common.AddressBean
import funddetails.view.common.AssetBean
import funddetails.view.component.AssetAllocationBean
import funddetails.view.component.AssetAllocationTableBean
import funddetails.view.component.AssetPaymentBean
import funddetails.view.component.AssetPaymentTableBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.Navigator
import network.usecase.CreateFundShareIssuanceUriUseCase
import network.usecase.GetAddressExplorerUseCase
import network.usecase.GetAssetExplorerUseCase

class FundDetailsViewModel(
    private val getFundDetails: GetFundDetailsUseCase,
    private val calculateAssetPayment: CalculateAssetPaymentUseCase,
    private val getAddressExplorer: GetAddressExplorerUseCase,
    private val getAssetExplorer: GetAssetExplorerUseCase,
    private val createFundShareIssuanceUri: CreateFundShareIssuanceUriUseCase,
    navigator: Navigator,
) {

    private val _fundDetailsState = mutableStateOf(FundDetailsState())
    private val _tradingState = mutableStateOf(TradingBean())

    val fundDetailState: State<FundDetailsState> = _fundDetailsState
    val tradingState: State<TradingBean> = _tradingState

    init {
        CoroutineScope(Job()).launch {
            initializeFundDetails(navigator.param, this)
        }.start()
    }

    private fun initializeFundDetails(address: String?, coroutineScope: CoroutineScope) {
        if (address == null) {
            _fundDetailsState.value = FundDetailsState(error = "No crypto fund address")
            return
        }

        getFundDetails(address)
            .onEach { result ->
                console.log(result)
                when (result) {
                    is Resource.Loading -> {
                        _fundDetailsState.value = FundDetailsState(isLoading = true)
                    }

                    is Resource.Error -> {
                        _fundDetailsState.value = FundDetailsState(error = result.message ?: "Unexpected error")
                    }

                    is Resource.Success -> {
                        result.data?.let { fundDetails ->
                            _fundDetailsState.value =
                                FundDetailsState(
                                    fundDetails = FundDetailsBean(
                                        address = AddressBean(
                                            value = fundDetails.address,
                                            explorerUrl = getAddressExplorer(fundDetails.address)
                                        ),
                                        totalShares = fundDetails.totalShares.toFormattedNumber(),
                                        shareAsset = AssetBean(
                                            hash = fundDetails.totalShares.asset.hash,
                                            symbol = fundDetails.totalShares.asset.name,
                                            explorerUrl = getAssetExplorer(fundDetails.totalShares.asset.hash)
                                        ),
                                        allocationTable = AssetAllocationTableBean(
                                            allocations = fundDetails.allocation.map { allocation ->
                                                AssetAllocationBean(
                                                    asset = AssetBean(
                                                        symbol = allocation.balance.asset.name,
                                                        hash = allocation.balance.asset.hash,
                                                        explorerUrl = getAssetExplorer(allocation.balance.asset.hash)
                                                    ),
                                                    balance = allocation.balance.toFormattedNumber(),
                                                    percentage = fundDetails.run {
                                                        allocation.adjustedPercentage.toFormattedPecentage()
                                                    }
                                                )
                                            }
                                        )
                                    )
                                )
                            _tradingState.value = TradingBean(
                                fundAddress = fundDetails.address,
                                sharesToBuy = "",
                                shareSymbol = fundDetails.totalShares.asset.name,
                                allocation = fundDetails.allocation,
                                totalShares = fundDetails.totalShares,
                                assetPaymentTable = AssetPaymentTableBean(
                                    assetPayments = fundDetails.allocation.map { allocation ->
                                        AssetPaymentBean(
                                            assetSymbol = allocation.balance.asset.name,
                                            amount = "0"
                                        )
                                    }
                                ),
                                assetRedemptionTable = AssetPaymentTableBean(
                                    assetPayments = fundDetails.allocation.map { allocation ->
                                        AssetPaymentBean(
                                            assetSymbol = allocation.balance.asset.name,
                                            amount = "0"
                                        )
                                    }
                                )
                            )
                        }
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun Balance.toFormattedNumber(): String {
        return toDouble().asDynamic().toFixed(asset.decimals).toString()
    }

    private fun Double.toFormattedPecentage(): String {
        return times(100).asDynamic().toFixed(2).toString() + "%"
    }

    fun updateAssetPayments(sharesToBuy: String) = with(tradingState.value) {
        val sharesToBuyAmount = sharesToBuy.toDoubleOrNull() ?: 0.0
        val assetPayments = calculateAssetPayment(allocation, totalShares, sharesToBuyAmount)

        _tradingState.value = copy(
            sharesToBuy = sharesToBuy,
            assetPaymentTable = AssetPaymentTableBean(
                assetPayments = assetPayments.map { payment ->
                    AssetPaymentBean(
                        assetSymbol = payment.asset.name,
                        amount = payment.toFormattedNumber()
                    )
                }
            ),
            assetPaymentUrl = createFundShareIssuanceUri(
                address = fundAddress,
                assets = assetPayments.associate { it.asset.hash to it.amount }.toMap()
            )
        )
    }

    fun updateRedemption(sharesToRedeem: String) = with(tradingState.value) {
        val sharesToRedeemAmount = sharesToRedeem.toDoubleOrNull() ?: 0.0
        val assetRedemption = calculateAssetPayment(allocation, totalShares, sharesToRedeemAmount)

        _tradingState.value = copy(
            sharesToRedeem = sharesToRedeem,
            assetRedemptionTable = AssetPaymentTableBean(
                assetPayments = assetRedemption.map { redemption ->
                    AssetPaymentBean(
                        assetSymbol = redemption.asset.name,
                        amount = redemption.toFormattedNumber()
                    )
                }
            )
        )
    }

}