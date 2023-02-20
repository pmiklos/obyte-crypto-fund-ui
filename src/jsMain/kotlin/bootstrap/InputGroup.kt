package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement


@Composable
fun InputGroup(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("input-group", "mb-3")
            attrs?.invoke(this)
        }, content = content
    )
}

@Composable
fun AddOn(text: String, attrs: AttrBuilderContext<HTMLSpanElement>? = null) {
    Span(
        attrs = {
            classes("input-group-text")
            attrs?.invoke(this)
        }
    ) {
        Text(text)
    }
}