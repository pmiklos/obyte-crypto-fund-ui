package funddetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import funddetails.components.AssetAllocationBean
import funddetails.components.AssetAllocationTableBean
import funddetails.components.AssetBean
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


        val assetAllocations = AssetAllocationTableBean(
            listOf(
                AssetAllocationBean(
                    AssetBean("BTC", "vApNsebTEPb3QDNNfyLsDB/iI5st9koMpAqvADzTw5A="),
                    "14.68",
                    "2.20279710"
                ),
                AssetAllocationBean(
                    AssetBean("ETC", "RF/ysZ/ZY4leyc3huUq1yFc0xTS0GdeFQu8RmXas4ys="),
                    "85.32",
                    "12.797202"
                ),
            )
        )

        _state.value = FundDetailsState(fundDetails = FundDetailsBean(address, "5432545", assetAllocations))
    }

}

data class FundDetailsBean(
    val address: String,
    val totalShares: String,
    val assetAllocations: AssetAllocationTableBean
)
