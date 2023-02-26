package funddetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import navigation.Navigator

class FundDetailsModel(navigator: Navigator) {

    private val _state = mutableStateOf(FundDetailsState())

    val state: State<FundDetailsState> = _state

    init {
        getFundDetails(navigator.param)
    }

    private fun getFundDetails(address: String?) {
        if (address == null) {
            _state.value = FundDetailsState(error = "No crypto fund address")
            return
        }

        _state.value = FundDetailsState(isLoading = true)

        _state.value = FundDetailsState(fundDetails = FundDetailsBean(address, "5432545"))
    }
}

data class FundDetailsBean(val address: String, val totalShares: String)
