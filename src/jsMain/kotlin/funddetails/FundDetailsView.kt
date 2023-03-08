package funddetails

import androidx.compose.runtime.Composable
import bootstrap.AddOn
import bootstrap.ButtonBlock
import bootstrap.ButtonPrimary
import bootstrap.Card
import bootstrap.CardBody
import bootstrap.CardHeader
import bootstrap.Col
import bootstrap.InputGroup
import bootstrap.Row
import bootstrap.TextInput
import bootstrap.Warning
import compose.Dd
import compose.Dl
import compose.Dt
import funddetails.components.AssetAllocationTable
import funddetails.components.AssetPaymentTable
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundDetails(fundDetailsViewModel: FundDetailsViewModel) {
    val state = fundDetailsViewModel.fundDetailState.value

    state.fundDetails?.let { fundDetails ->
        Row {
            Col(6) {
                FundInfo(fundDetails)
            }
            Col(6) {
                Trade(fundDetailsViewModel.tradingState.value) {
                    fundDetailsViewModel.updatePayment(it)
                }
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
                    A(href = "https://explorer.obyte.org/#${fundDetails.address}") {
                        Text(fundDetails.address)
                    }
                }
                Dt {
                    Text("Total Shares")
                }
                Dd {
                    Text(fundDetails.totalShares + " ")
                    A(href = "https://explorer.obyte.org/#${fundDetails.shareAsset}") {
                        Text(fundDetails.shareSymbol)
                    }
                }
            }
            AssetAllocationTable(
                fundDetails.allocationTable
            )
        }
    }
}

@Composable
private fun Trade(
    buyShares: TradingBean,
    onPurchaseAmountChange: (String) -> Unit
) {
    Card {
        CardHeader {
            Text("Trade")
        }
        CardBody {
            Form {
                InputGroup {
                    TextInput {
                        placeholder("Enter the number of shares")
                        value(buyShares.sharesToBuy)
                        onInput { onPurchaseAmountChange(it.value) }
                    }
                    AddOn(buyShares.shareSymbol)
                }

                AssetPaymentTable(buyShares.assetPaymentTable)

                ButtonBlock {
                    ButtonPrimary(attrs = {
                        if (buyShares.sharesToBuy == null) {
                            disabled()
                        }
                    }) {
                        Text("Buy Shares")
                    }
                }
            }
        }
    }
}