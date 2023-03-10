package funddetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import common.Resource
import funddetails.components.AssetAllocationBean
import funddetails.components.AssetAllocationTableBean
import funddetails.components.AssetBean
import funddetails.components.AssetPaymentBean
import funddetails.components.AssetPaymentTableBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.Navigator

class FundDetailsViewModel(
    private val getFundDetails: GetFundDetailsUseCase,
    private val calculateAssetPayment: CalculateAssetPaymentUseCase,
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
                                        address = fundDetails.address,
                                        totalShares = fundDetails.totalShares.toFormattedNumber(),
                                        shareAsset = fundDetails.totalShares.asset.hash,
                                        shareSymbol = fundDetails.totalShares.asset.name,
                                        allocationTable = AssetAllocationTableBean(
                                            allocations = fundDetails.allocation.map { allocation ->
                                                AssetAllocationBean(
                                                    asset = AssetBean(
                                                        symbol = allocation.balance.asset.name,
                                                        hash = allocation.balance.asset.hash
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
            )
        )
    }

}

data class FundDetailsBean(
    val address: String,
    val totalShares: String,
    val shareAsset: String,
    val shareSymbol: String,
    val allocationTable: AssetAllocationTableBean,
)

data class TradingBean(
    val sharesToBuy: String = "",
    val shareSymbol: String = "",
    val allocation: List<AssetAllocation> = emptyList(),
    val totalShares: Balance = Balance(Asset("", "", 0), 0),
    val assetPaymentTable: AssetPaymentTableBean = AssetPaymentTableBean(emptyList())
)