package fundlist.domain

data class Fund(
    val address: String,
    val portfolio: List<Portfolio>
)
