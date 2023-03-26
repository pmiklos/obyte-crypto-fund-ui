package fundlist.view

import androidx.compose.runtime.Composable
import bootstrap.Info
import bootstrap.ListGroup
import bootstrap.ListGroupItem
import bootstrap.ListGroupItemHeading
import bootstrap.Warning
import navigation.Screen
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundList(viewModel: FundListViewModel) {
    val state = viewModel.state.value
    val funds = viewModel.funds
    ListGroup {
        funds.forEach { fund ->
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

