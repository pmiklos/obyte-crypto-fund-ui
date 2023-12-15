package fundlist.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        state = FundListState(isLoading = true)

        getFundTypes().forEach { baseFund ->

            getFunds(baseFund).onEach { result ->
                when (result) {
                    is Resource.Loading -> result.data?.let { fund ->
                        state = FundListState()
                        funds.add(
                            FundSummaryBean(
                                address = fund.address,
                                summary = fund.assetName,
                                description = fund.description,
                                baseFund = baseFund.address,
                                version = baseFund.version,
                                loading = true
                            )
                        )
                    }

                    is Resource.Success -> result.data?.let { finalizedFund ->
                        state = FundListState()

                        val index = funds.indexOfFirst {
                            it.address == finalizedFund.address
                        }

                        if (index >= 0) {
                            funds[index] = FundSummaryBean(
                                address = finalizedFund.address,
                                summary = finalizedFund.assetName + finalizedFund.portfolio.joinToString(
                                    separator = ", ",
                                    prefix = " (",
                                    postfix = ")"
                                ) {
                                    it.assetName
                                },
                                description = finalizedFund.description,
                                baseFund = baseFund.address,
                                version = baseFund.version
                            )
                        }
                    }

                    is Resource.Error -> result.data?.let { failedFund ->
                        funds.removeAll {
                            it.address == failedFund.address
                        }

                        funds.add(
                            FundSummaryBean(
                                address = failedFund.address,
                                summary = failedFund.assetName,
                                description = failedFund.description,
                                baseFund = baseFund.address,
                                version = baseFund.version,
                                error = result.message ?: "Invalid fund"
                            )
                        )
                    } ?: {
                        state = FundListState(error = result.message ?: "Unexpected error")
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
    val loading: Boolean = false,
    val error: String = ""
) {
    val valid: Boolean = error.isEmpty()
}