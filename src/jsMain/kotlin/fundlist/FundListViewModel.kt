package fundlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import common.Resource
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
                        _funds.addAll(newFunds.map {
                            FundSummaryBean(
                                address = it.address,
                                description = it.portfolio.joinToString(" ") {
                                    "${it.assetName} (${it.percentage}%)"
                                },
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
    val description: String,
    val baseFund: String,
    val version: String,
)