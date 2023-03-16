package funddetails.view.component

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr

@Composable
fun AssetPaymentTable(model: AssetPaymentTableBean, label: String) {
    Table( attrs = {
        classes("table")
    }) {
        Thead {
            Tr {
                Th {
                    Text("Asset")
                }
                Th {
                    Text(label)
                }
            }
        }
        Tbody {
            model.assetPayments.forEach { allocation ->
                Tr {
                    Td {
                        Text(allocation.assetSymbol)
                    }
                    Td {
                        Text(allocation.amount)
                    }
                }
            }
        }
    }
}

data class AssetPaymentTableBean(val assetPayments: List<AssetPaymentBean>)
data class AssetPaymentBean(val assetSymbol: String, val amount: String)