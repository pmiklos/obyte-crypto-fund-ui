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
import funddetails.components.AssetAllocationTable
import navigation.Screen
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundDetails(fundDetailsViewModel: FundDetailsViewModel) {
    val state = fundDetailsViewModel.state.value

    state.fundDetails?.let { fundDetails ->
        Row {
            Col(6) {
                FundInfo(fundDetails)
            }
            Col(6) {
                AssetAllocationTable(
                    fundDetails.assetAllocations
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
