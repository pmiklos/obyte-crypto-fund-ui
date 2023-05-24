package funddetails.view.component

import androidx.compose.runtime.Composable
import funddetails.view.common.AssetBean
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr

@Composable
fun AssetAllocationTable(model: AssetAllocationTableBean) {
    Table( attrs = {
        classes("table", "table-bordered")
    }) {
        Thead {
            Tr {
                Th {
                    Text("Asset")
                }
                Th {
                    Text("Weight")
                }
                Th {
                    Text("Balance")
                }
            }
        }
        Tbody {
            model.allocations.forEach { allocation ->
                Tr {
                    Td {
                        A(href=allocation.asset.explorerUrl, attrs = {
                            target(ATarget.Blank)
                        }) {
                            Text(allocation.asset.symbol)
                        }
                    }
                    Td {
                        Text(allocation.percentage)
                    }
                    Td {
                        Text(allocation.balance)
                    }
                }
            }
        }
    }
}

data class AssetAllocationTableBean(val allocations: List<AssetAllocationBean>)
data class AssetAllocationBean(val asset: AssetBean, val percentage: String, val balance: String)