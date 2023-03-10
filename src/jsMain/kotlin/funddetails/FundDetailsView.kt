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
import bootstrap.NavTab
import bootstrap.NavTabs
import bootstrap.Row
import bootstrap.TabContent
import bootstrap.TabPane
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
                Trade(
                    tradingState = fundDetailsViewModel.tradingState.value,
                    onPurchaseAmountChange = { fundDetailsViewModel.updateAssetPayments(it) },
                    onRedemptionAmountChange = { fundDetailsViewModel.updateRedemption(it) },
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
    tradingState: TradingBean,
    onPurchaseAmountChange: (String) -> Unit,
    onRedemptionAmountChange: (String) -> Unit
) {
    Card {
        CardHeader {
            Text("Trade")
        }
        CardBody {
            NavTabs {
                NavTab("buy", "Buy", active = true, attrs = { classes("w-50") })
                NavTab("redeem", "Redeem", attrs = { classes("w-50") })
            }
            TabContent {
                TabPane("buy", active = true) {
                    BuySharesPane(tradingState, onPurchaseAmountChange)
                }
                TabPane("redeem") {
                    RedemptionPane(tradingState, onRedemptionAmountChange)
                }
            }
        }
    }
}

@Composable
private fun BuySharesPane(
    tradingState: TradingBean,
    onPurchaseAmountChange: (String) -> Unit
) {
    Form {
        InputGroup {
            TextInput {
                placeholder("Shares to buy")
                value(tradingState.sharesToBuy)
                onInput { onPurchaseAmountChange(it.value) }
            }
            AddOn(tradingState.shareSymbol)
        }

        AssetPaymentTable(tradingState.assetPaymentTable, label = "Payable")

        ButtonBlock {
            ButtonPrimary(attrs = {
                if (!tradingState.sharesBuyable) {
                    disabled()
                }
            }) {
                Text("Buy Shares")
            }
        }
    }
}

@Composable
private fun RedemptionPane(
    tradingState: TradingBean,
    onRedemptionAmountChange: (String) -> Unit
) {
    Form {
        InputGroup {
            TextInput {
                placeholder("Shares to redeem")
                value(tradingState.sharesToRedeem)
                onInput { onRedemptionAmountChange(it.value) }
                if (!tradingState.sharesRedeemable) {
                    classes("text-danger")
                }
            }
            AddOn(tradingState.shareSymbol)
        }

        AssetPaymentTable(tradingState.assetRedemptionTable, label = "Redeemable")

        ButtonBlock {
            ButtonPrimary(attrs = {
                if (!tradingState.sharesRedeemable) {
                    disabled()
                }
            }) {
                Text("Redeem Assets")
            }
        }
    }
}