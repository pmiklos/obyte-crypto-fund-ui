package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement

@Composable
fun NavBar(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Nav(
        attrs = {
            classes("navbar", "navbar-expand-lg")
            attrs?.invoke(this)
        }
    ) {
        Div(
            attrs = {
                classes("container-fluid")
            },
            content = content
        )
    }
}

@Composable
fun NavBarBrand(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            classes("navbar-brand")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavBarToggler(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLButtonElement>? = null
) {
    Button(
        attrs = {
            classes("navbar-toggler")
            attr("data-bs-toggle", "collapse")
            attr("data-bs-target", "#navbarSupportedContent")
            attr("aria-controls", "navbarSupportedContent")
            attr("aria-expanded", "false")
            attr("aria-label", "Toggle navigation")
            attrs?.invoke(this)
        }
    ) {
        if (content != null) {
            content()
        } else {
            NavBarTogglerIcon()
        }
    }
}

@Composable
fun NavBarTogglerIcon() {
    Span(
        attrs = {
            classes("navbar-toggler-icon")
        }
    )
}

@Composable
fun NavBarCollapse(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(
        attrs = {
            id("navbarSupportedContent")
            classes("collapse", "navbar-collapse")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavBarNav(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLUListElement>? = null
) {
    Ul(
        attrs = {
            classes("navbar-nav", "me-auto", "mb-2", "mb-lg-0")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavBarNavItem(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLLIElement>? = null
) {
    Li(
        attrs = {
            classes("nav-item")
            attrs?.invoke(this)
        },
        content = content
    )
}

@Composable
fun NavBarNavLink(
    href: String,
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLAnchorElement>? = null
) {
    A(
        attrs = {
            classes("nav-link")
            attrs?.invoke(this)
        },
        href = href,
        content = content
    )
}