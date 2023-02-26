package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun Card(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("card", "mb-3")
            attrs?.invoke(this)
        }, content = content
    )
}

@Composable
fun CardHeader(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("card-header")
            attrs?.invoke(this)
        }, content = content
    )
}

@Composable
fun CardBody(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("card-body")
            attrs?.invoke(this)
        }, content = content
    )
}

@Composable
fun CardFooter(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("card-footer")
            attrs?.invoke(this)
        }, content = content
    )
}
