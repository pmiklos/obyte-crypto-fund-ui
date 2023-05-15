package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement


@Composable
fun Flex(left: ContentBuilder<HTMLDivElement>, right: ContentBuilder<HTMLDivElement>) {
    Div(attrs = { classes("d-flex") }) {
        Div(attrs = { classes("d-flex") }, content = left)
        Div(attrs = { classes("d-flex", "ms-auto", "w-auto") }, content = right)
    }
}

@Composable
fun FlexResponsive(left: ContentBuilder<HTMLDivElement>, right: ContentBuilder<HTMLDivElement>) {
    Div(attrs = { classes("d-flex", "flex-column", "flex-md-row") }) {
        Div(attrs = { classes("d-flex") }, content = left)
        Div(attrs = { classes("d-flex", "ms-md-auto", "w-auto") }, content = right)
    }
}
