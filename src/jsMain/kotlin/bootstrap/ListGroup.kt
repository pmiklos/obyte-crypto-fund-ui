package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H5
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement

@Composable
fun ListGroup(
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("list-group")
        }, content = content
    )
}

@Composable
fun ListGroupItem(
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    content: ContentBuilder<HTMLAnchorElement>? = null
) {
    A(content = content, attrs = {
        classes("list-group-item", "list-group-item-action")
        attrs?.invoke(this)
    })
}

@Composable
fun ListGroupItemHeading(
    headLine: String,
    note: String = ""
) {
    Div(
        attrs = {
            classes("d-flex", "w-100", "justify-content-between")
        }
    ) {
        H5(
            attrs = {
                classes("mb-1")
            }
        ) {
            Text(headLine)
        }
        Small {
            Text(note)
        }
    }
}