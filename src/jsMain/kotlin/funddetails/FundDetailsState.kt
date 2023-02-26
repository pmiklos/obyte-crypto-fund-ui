package funddetails

data class FundDetailsState(
    val isLoading: Boolean = false,
    val fundDetails: FundDetailsBean? = null,
    val error: String = ""
)