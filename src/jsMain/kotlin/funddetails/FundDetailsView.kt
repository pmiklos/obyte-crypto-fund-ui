package funddetails

import androidx.compose.runtime.Composable
import bootstrap.Card
import bootstrap.CardBody
import bootstrap.CardFooter
import bootstrap.CardHeader
import bootstrap.Col
import bootstrap.Row
import bootstrap.Warning
import compose.Dd
import compose.Dl
import compose.Dt
import funddetails.components.AssetAllocationBean
import funddetails.components.AssetAllocationTable
import funddetails.components.AssetAllocationTableModel
import funddetails.components.AssetBean
import navigation.Screen
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundDetails(fundDetailsModel: FundDetailsModel) {
    val state = fundDetailsModel.state.value

    state.fundDetails?.let { fundDetails ->
        Row {
            Col(6) {
                FundInfo(fundDetails)
            }
            Col(6) {
                AssetAllocationTable(
                    // TODO move allocation table model to FundDetailsModel
                    AssetAllocationTableModel(
                        listOf(
                            AssetAllocationBean(AssetBean("BTC", "vApNsebTEPb3QDNNfyLsDB/iI5st9koMpAqvADzTw5A="), "14.68", "2.20279710"),
                            AssetAllocationBean(AssetBean("ETC", "RF/ysZ/ZY4leyc3huUq1yFc0xTS0GdeFQu8RmXas4ys="), "85.32", "12.797202"),
                        )
                    )
                )
            }
        }
    }

    if (state.error.isNotBlank()) {
        Warning(state.error)
    }

    if (state.isLoading) {
        Text("Loading...")
    }
}

@Composable
private fun FundInfo(fundDetails: FundDetailsBean) {
    Card {
        CardHeader {
            Text("Fund Information")
        }
        CardBody {
            Dl {
                Dt {
                    Text("Fund Address")
                }
                Dd {
                    Text(fundDetails.address)
                }
                Dt {
                    Text("Total Shares")
                }
                Dd {
                    Text(fundDetails.totalShares)
                }
            }
        }
        CardFooter {
            A(href = Screen.Home.href) {
                Text("back")
            }
        }
    }
}
