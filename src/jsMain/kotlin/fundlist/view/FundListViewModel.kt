package fundlist.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    var funds = mutableStateListOf<FundSummaryBean>()
        private set

    var state by mutableStateOf(FundListState())
        private set

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
                        state = FundListState()
                        funds.addAll(newFunds.map { fund ->
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
                        state = FundListState(error = result.message ?: "Unexpected error")
                    }

                    is Resource.Loading -> {
                        state = FundListState(isLoading = true)
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