package funddetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import common.Resource
import common.powerOf
import funddetails.components.AssetAllocationBean
import funddetails.components.AssetAllocationTableBean
import funddetails.components.AssetBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.Navigator

class FundDetailsViewModel(
    private val getFundDetails: GetFundDetailsUseCase,
    navigator: Navigator,
) {

    private val _state = mutableStateOf(FundDetailsState())

    val state: State<FundDetailsState> = _state

    init {
        CoroutineScope(Job()).launch {
            initializeFundDetails(navigator.param, this)
        }.start()
    }

    private fun initializeFundDetails(address: String?, coroutineScope: CoroutineScope) {
        if (address == null) {
            _state.value = FundDetailsState(error = "No crypto fund address")
            return
        }

        getFundDetails(address)
            .onEach { result ->
                console.log(result)
                when (result) {
                    is Resource.Loading -> {
                        _state.value = FundDetailsState(isLoading = true)
                    }

                    is Resource.Error -> {
                        _state.value = FundDetailsState(error = result.message ?: "Unexpected error")
                    }

                    is Resource.Success -> {
                        result.data?.let { fundDetails ->
                            _state.value =
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
                        }
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun Balance.toFormattedNumber(): String {
        val divisor = 10.powerOf(asset.decimals)
        return amount.toDouble().div(divisor).asDynamic().toFixed(asset.decimals).toString()
    }

    private fun Double.toFormattedPecentage(): String {
        return times(100).asDynamic().toFixed(2).toString() + "%"
    }

}

data class FundDetailsBean(
    val address: String,
    val totalShares: String,
    val shareAsset: String,
    val shareSymbol: String,
    val allocationTable: AssetAllocationTableBean
)
