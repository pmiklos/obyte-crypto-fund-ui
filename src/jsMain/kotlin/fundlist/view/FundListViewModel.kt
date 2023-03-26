package fundlist.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import common.Resource
import fundlist.usecase.GetFundTypesUseCase
import fundlist.usecase.GetFundsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FundListViewModel(
    private val getFundTypes: GetFundTypesUseCase,
    private val getFunds: GetFundsUseCase
) {
    private val _funds = mutableStateListOf<FundSummaryBean>()
    private val _state = mutableStateOf(FundListState())
    val funds: SnapshotStateList<FundSummaryBean> = _funds
    val state: State<FundListState> = _state

    init {
        CoroutineScope(Job()).launch {
            fetchAllFunds(this)
        }.start()
    }

    private suspend fun fetchAllFunds(coroutineScope: CoroutineScope) {
        getFundTypes().forEach { baseFund ->

            getFunds(baseFund).onEach { result ->
                when (result) {
                    is Resource.Success -> result.data?.let { newFunds ->
                        _state.value = FundListState()
                        _funds.addAll(newFunds.map { fund ->
                            FundSummaryBean(
                                address = fund.address,
                                summary = fund.assetName + fund.portfolio.joinToString(
                                    separator = ", ",
                                    prefix = " (",
                                    postfix = ")"
                                ) {
                                    it.assetName
                                },
                                description = fund.description,
                                baseFund = baseFund.address,
                                version = baseFund.version
                            )
                        })
                    }

                    is Resource.Error -> {
                        _state.value = FundListState(error = result.message ?: "Unexpected error")
                    }

                    is Resource.Loading -> {
                        _state.value = FundListState(isLoading = true)
                    }
                }
            }.launchIn(coroutineScope)
        }
    }

}

data class FundSummaryBean(
    val address: String,
    val summary: String,
    val description: String,
    val baseFund: String,
    val version: String,
)