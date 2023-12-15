package fundlist.view

import androidx.compose.runtime.Composable
import bootstrap.Info
import bootstrap.ListGroup
import bootstrap.ListGroupItem
import bootstrap.ListGroupItemHeading
import bootstrap.Warning
import navigation.Screen
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundList(viewModel: FundListViewModel) {
    val state = viewModel.state
    val funds = viewModel.funds
    ListGroup {
        funds.forEach { fund ->
            if (fund.valid) {
                FundSummary(fund)
            } else {
                InvalidFundSummary(fund)
            }
        }
    }
    if (state.error.isNotBlank()) {
        Warning(state.error)
    }
    if (state.isLoading) {
        Info("Loading...")
    }
}

@Composable
private fun FundSummary(fund: FundSummaryBean) {
    ListGroupItem(
        attrs = {
            href(Screen.Details.href(fund.address))
        }
    ) {
        ListGroupItemHeading(fund.summary, fund.version)
        P(attrs = {
            classes("mb-1")
        }) {
            Text(fund.description)
        }
        Small { Text(fund.address) }

        if (fund.loading) {
            IndefiniteProgressBar()
        }
    }
}

@Composable
private fun InvalidFundSummary(fund: FundSummaryBean) {
    ListGroupItem {
        P(attrs = {
            classes("mb-1")
        }) {
            Text(fund.error)
        }
        Small { Text(fund.address) }
    }
}

@Composable
private fun IndefiniteProgressBar() {
    Div(attrs = { classes("progress-bar") }) {
        Div(attrs = { classes("progress-bar-value") })
    }
}