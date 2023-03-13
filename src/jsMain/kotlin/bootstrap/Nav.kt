package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement

@Composable
fun NavPills(
    attrs: AttrBuilderContext<HTMLUListElement>? = null,
    content: ContentBuilder<HTMLUListElement>? = null
) {
    Ul(
        attrs = {
            classes("nav", "nav-pills", "mb-3")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavPill(
    target: String,
    label: String,
    active: Boolean = false,
    attrs: AttrBuilderContext<HTMLLIElement>? = null,
) {
    Li(
        attrs = {
            classes("nav-item")
            attrs?.invoke(this)
        }
    ) {
        Button(
            attrs = {
                id("$target-tab")
                attr("data-bs-toggle", "pill")
                attr("data-bs-target", "#${target}")
                attr("role", "tab")
                if (active) {
                    classes("nav-link", "active")
                } else {
                    classes("nav-link")
                }
            }
        ) {
            Text(label)
        }
    }
}

@Composable
fun NavTabs(
    attrs: AttrBuilderContext<HTMLUListElement>? = null,
    content: ContentBuilder<HTMLUListElement>? = null
) {
    Ul(
        attrs = {
            classes("nav", "nav-tabs", "mb-3")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavTab(
    target: String,
    label: String,
    active: Boolean = false,
    attrs: AttrBuilderContext<HTMLLIElement>? = null,
) {
    Li(
        attrs = {
            classes("nav-item")
            attrs?.invoke(this)
        }
    ) {
        Button(
            attrs = {
                id("$target-tab")
                attr("data-bs-toggle", "tab")
                attr("data-bs-target", "#${target}")
                attr("role", "tab")
                if (active) {
                    classes("nav-link", "active", "w-100")
                } else {
                    classes("nav-link", "w-100")
                }
            }
        ) {
            Text(label)
        }
    }
}


@Composable
fun TabContent(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("tab-content")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun TabPane(
    id: String,
    active: Boolean = false,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            id(id)
            attr("role", "tabpanel")
            tabIndex(0)
            if (active) {
                classes("tab-pane", "fade", "show", "active")
            } else {
                classes("tab-pane", "fade")
            }
            attrs?.invoke(this)
        },
        content = content
    )
}