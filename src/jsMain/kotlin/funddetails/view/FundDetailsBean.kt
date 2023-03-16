package funddetails.view

import funddetails.view.component.AssetAllocationTableBean

data class FundDetailsBean(
    val address: String,
    val totalShares: String,
    val shareAsset: String,
    val shareSymbol: String,
    val allocationTable: AssetAllocationTableBean,
)