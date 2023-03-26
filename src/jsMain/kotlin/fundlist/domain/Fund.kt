package fundlist.domain

data class Fund(
    val address: String,
    val assetName: String,
    val description: String,
    val portfolio: List<Portfolio>
)
