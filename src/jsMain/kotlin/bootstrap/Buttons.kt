package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement

@Composable
fun ButtonPrimary(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    content: ContentBuilder<HTMLButtonElement>? = null
) {
    Button(attrs = {
        classes("btn", "btn-primary")
        attrs?.invoke(this)
    }, content = content)
}

@Composable
fun ButtonOutlineInfo(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    content: ContentBuilder<HTMLButtonElement>? = null
) {
    Button(attrs = {
        classes("btn", "btn-outline-info")
        attrs?.invoke(this)
    }, content = content)
}

@Composable
fun ButtonBlock(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(attrs = {
        classes("d-grid", "gap-2", "col-lg-6", "col-12", "mx-auto")
        attrs?.invoke(this)
    }, content = content)
}