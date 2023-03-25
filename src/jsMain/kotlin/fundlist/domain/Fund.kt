package fundlist.domain

data class Fund(
    val address: String,
    val assetName: String,
    val portfolio: List<Portfolio>
)
