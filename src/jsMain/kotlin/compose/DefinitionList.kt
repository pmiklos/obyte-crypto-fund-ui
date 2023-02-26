package compose

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.Element
import org.w3c.dom.HTMLDListElement
import org.w3c.dom.HTMLElement

private open class ElementBuilderImplementation<TElement : Element>(private val tagName: String) :
    ElementBuilder<TElement> {
    private val el: Element by lazy { document.createElement(tagName) }

    @Suppress("UNCHECKED_CAST")
    override fun create(): TElement = el.cloneNode() as TElement
}

private val Dl: ElementBuilder<HTMLDListElement> = ElementBuilderImplementation("dl")
private val Dt: ElementBuilder<HTMLElement> = ElementBuilderImplementation("dt")
private val Dd: ElementBuilder<HTMLElement> = ElementBuilderImplementation("dd")

@Composable
fun Dl(
    attrs: AttrBuilderContext<HTMLDListElement>? = null,
    content: ContentBuilder<HTMLDListElement>? = null
) = TagElement(elementBuilder = Dl, applyAttrs = attrs, content = content)

@Composable
fun Dt(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = Dt, applyAttrs = attrs, content = content)

@Composable
fun Dd(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = Dd, applyAttrs = attrs, content = content)
