package fundlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import bootstrap.Info
import bootstrap.ListGroup
import bootstrap.ListGroupItem
import bootstrap.ListGroupItemHeading
import bootstrap.Warning
import common.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundList(viewModel: FundListViewModel) {
    val state = viewModel.state.value
    val funds = viewModel.funds
    ListGroup {
        funds.forEach { fund ->
            ListGroupItem {
                ListGroupItemHeading(fund.description, fund.version)
                P(attrs = {
                    classes("mb-1")
                }) {
                    Text("More information about this fund")
                }
                Small { Text(fund.address) }
            }
        }
    }
    if (state.error.isNotBlank()) {
        Warning(state.error)
    }
    if (state.isLoading) {
        Info("Loading...")
    }
}

data class FundSummaryBean(
    val address: String,
    val description: String,
    val baseFund: String,
    val version: String,
)

data class FundListState(
    val isLoading: Boolean = false,
    val error: String = ""
)

class FundListViewModel(
    private val getFundTypes: GetFundTypesUseCase,
    private val getFunds: GetFundsUseCase,
    private val coroutineScope: CoroutineScope
) {
    private val _funds = mutableStateListOf<FundSummaryBean>()
    private val _state = mutableStateOf(FundListState())
    val funds: SnapshotStateList<FundSummaryBean> = _funds
    val state: State<FundListState> = _state

    init {
        fetchAllFunds()
    }

    private fun fetchAllFunds() = coroutineScope.launch {
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
            }.launchIn(this)
        }
    }

}