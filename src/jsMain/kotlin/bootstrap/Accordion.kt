package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement

@Composable
fun Accordion(id: String, content: (@Composable AccordionBuilder.() -> Unit)? = null) {
    Div(attrs = {
        id(id)
        classes("accordion")
    }) {
        val accordionBuilder = AccordionBuilder(id)
        content?.invoke(accordionBuilder)
    }
}

class AccordionBuilder(private val accordionId: String) {

    @Composable
    fun item(
        id: String? = null,
        title: String,
        expand: Boolean = false,
        content: ContentBuilder<HTMLDivElement>? = null
    ) {
        AccordionItem(id ?: title.cssIdentifier(), accordionId, title, expand, content)
    }

    private fun String.cssIdentifier(): String {
        return this.replace(" ", "-").replace("[^a-zA-Z0-9-]".toRegex(), "").toLowerCase()
    }
}

@Composable
fun AccordionItem(
    id: String,
    accordionId: String,
    title: String,
    expand: Boolean = false,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(attrs = { classes("accordion-item") }) {
        H2(attrs = { classes("accordion-header") }) {
            Button(attrs = {
                classes("accordion-button")
                if (expand) {
                    attr("aria-expanded", "true")
                } else {
                    classes("collapsed")
                    attr("aria-expanded", "false")
                }
                attr("data-bs-toggle", "collapse")
                attr("data-bs-target", "#$id")
                attr("aria-controls", id)
            }) {
                Text(title)
            }
        }
        Div(attrs = {
            id(id)
            classes("accordion-collapse", "collapse")
            if (expand) {
                classes("show")
            }
            attr("data-bs-parent", "#$accordionId")
        }) {
            Div(
                attrs = { classes("accordion-body") },
                content = content
            )
        }
    }
}