package funddetails.view

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import common.Resource
import common.movePointRight
import funddetails.domain.Balance
import funddetails.domain.WalletRepository
import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.CreateAssetRedemptionUriUseCase
import funddetails.usecase.CreateFundShareIssuanceUriUseCase
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
import network.usecase.GetAddressExplorerUseCase
import network.usecase.GetAssetExplorerUseCase

class FundDetailsViewModel(
    private val getFundDetails: GetFundDetailsUseCase,
    private val calculateAssetPayment: CalculateAssetPaymentUseCase,
    private val getAddressExplorer: GetAddressExplorerUseCase,
    private val getAssetExplorer: GetAssetExplorerUseCase,
    private val createFundShareIssuanceUri: CreateFundShareIssuanceUriUseCase,
    private val createAssetRedemptionUri: CreateAssetRedemptionUriUseCase,
    private val walletRepository: WalletRepository) {

    var fundDetailsState by mutableStateOf(FundDetailsState())
        private set

    var tradingState by mutableStateOf(TradingBean())
        private set

    fun loadFund(address: String?) {
        CoroutineScope(Job()).launch {
            initializeFundDetails(address, this)
        }.start()
    }

    private fun initializeFundDetails(address: String?, coroutineScope: CoroutineScope) {
        if (address.isNullOrBlank()) {
            fundDetailsState = FundDetailsState(error = "No crypto fund address")
            return
        }

        getFundDetails(address)
            .onEach { result ->
                console.log(result)
                when (result) {
                    is Resource.Loading -> {
                        fundDetailsState = FundDetailsState(isLoading = true)
                    }

                    is Resource.Error -> {
                        fundDetailsState = FundDetailsState(error = result.message ?: "Unexpected error")
                    }

                    is Resource.Success -> {
                        result.data?.let { fundDetails ->
                            fundDetailsState =
                                FundDetailsState(
                                    fundDetails = FundDetailsBean(
                                        address = AddressBean(
                                            value = fundDetails.address,
                                            explorerUrl = getAddressExplorer(fundDetails.address)
                                        ),
                                        description = fundDetails.description,
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
                            tradingState = TradingBean(
                                walletAddress = walletRepository.getWallet(),
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

    fun updateWalletAddress(address: String) = with(tradingState) {
        tradingState = copy(
            walletAddress = address
        )
        updateAssetPayments(sharesToBuy)
        updateRedemption(sharesToRedeem)
    }

    fun updateAssetPayments(sharesToBuy: String) = with(tradingState) {
        val sharesToBuyAmount = sharesToBuy.toDoubleOrNull() ?: 0.0
        val assetPayments = calculateAssetPayment(allocation, sharesToBuyAmount)

        tradingState = copy(
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
                fromAddress = walletAddress,
                assetPayments = assetPayments.associate { it.asset.hash to it.amount }.toMap()
            )
        )
    }

    fun updateRedemption(sharesToRedeem: String) = with(tradingState) {
        val sharesToRedeemAmount = sharesToRedeem.toDoubleOrNull() ?: 0.0
        val assetRedemption = calculateAssetPayment(allocation, sharesToRedeemAmount)

        tradingState = copy(
            sharesToRedeem = sharesToRedeem,
            assetRedemptionTable = AssetPaymentTableBean(
                assetPayments = assetRedemption.map { redemption ->
                    AssetPaymentBean(
                        assetSymbol = redemption.asset.name,
                        amount = redemption.toFormattedNumber()
                    )
                }
            ),
            assetRedemptionUrl = createAssetRedemptionUri(
                address = fundAddress,
                fromAddress = walletAddress,
                shareAsset = totalShares.asset.hash,
                shareAmount = sharesToRedeemAmount.movePointRight(totalShares.asset.decimals).toLong()
            )
        )
    }

}