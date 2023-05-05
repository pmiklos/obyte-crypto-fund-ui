package funddetails.view

import funddetails.view.common.AddressBean
import funddetails.view.common.AssetBean
import funddetails.view.component.AssetAllocationTableBean

data class FundDetailsBean(
    val address: AddressBean,
    val description: String,
    val totalShares: String,
    val shareAsset: AssetBean,
    val allocationTable: AssetAllocationTableBean,
)