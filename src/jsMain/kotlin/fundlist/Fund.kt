package fundlist

data class Fund(
    val address: String,
    val portfolio: List<Portfolio>
)

data class Portfolio(
    val assetName: String,
    val percentage: Double
)