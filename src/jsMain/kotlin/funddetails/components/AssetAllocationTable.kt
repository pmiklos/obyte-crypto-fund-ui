package funddetails.components

import androidx.compose.runtime.Composable
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
                    Text("Allocation")
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
                        A(href="https://explorer.obyte.org/#${allocation.asset.hash}") {
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

class AssetAllocationTableBean(
    val allocations: List<AssetAllocationBean>) {
}

data class AssetBean(val symbol: String, val hash: String)
data class AssetAllocationBean(val asset: AssetBean, val percentage: String, val balance: String)