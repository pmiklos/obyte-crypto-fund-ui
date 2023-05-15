package funddetails.view

import androidx.compose.runtime.Composable
import bootstrap.AddOn
import bootstrap.ButtonBlock
import bootstrap.ButtonLinkPrimary
import bootstrap.Card
import bootstrap.CardBody
import bootstrap.CardHeader
import bootstrap.Col
import bootstrap.Flex
import bootstrap.Icon
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
import funddetails.view.component.AssetAllocationTable
import funddetails.view.component.AssetPaymentTable
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundDetails(fundDetailsViewModel: FundDetailsViewModel) {
    val state = fundDetailsViewModel.fundDetailState.value

    state.fundDetails?.let { fundDetails ->
        Row {
            Col(8) {
                FundInformationPane(fundDetails)
            }
            Col(4) {
                TradingPane(
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
private fun FundInformationPane(fundDetails: FundDetailsBean) {
    Div(
        attrs = {
            classes("mb-3", "p-3", "bg-body-tertiary", "rounded")
            style {
                property("--bs-bg-opacity", "0.5")
            }
        }
    ) {
        Flex(
            left = {
                H2 {
                    Text(fundDetails.shareAsset.symbol)
                }
            },
            right = {
                A(href = fundDetails.address.explorerUrl, attrs = {
                    target(ATarget.Blank)
                }){
                    Icon(Icon.BOX_ARROW_UP_RIGHT)
                }
            }
        )

        Dl {
            Dt {
                Text("Fund Description")
            }
            Dd {
                Text(fundDetails.description)
            }
            Dt {
                Text("Total Shares")
            }
            Dd {
                Text(fundDetails.totalShares + " ")
                A(href = fundDetails.shareAsset.explorerUrl, attrs = {
                    target(ATarget.Blank)
                }) {
                    Text(fundDetails.shareAsset.symbol)
                }
            }
        }
        AssetAllocationTable(
            fundDetails.allocationTable
        )
    }
}

@Composable
private fun TradingPane(
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
            ButtonLinkPrimary(href = tradingState.assetPaymentUrl, attrs = {
                if (!tradingState.sharesBuyable) {
                    classes("disabled")
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
            ButtonLinkPrimary(href = tradingState.assetRedemptionUrl, attrs = {
                if (!tradingState.sharesRedeemable) {
                    classes("disabled")
                }
            }) {
                Text("Redeem Assets")
            }
        }
    }
}