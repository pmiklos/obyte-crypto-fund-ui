package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.I
import org.w3c.dom.HTMLElement

enum class Icon(val cssClass: String) {

    BOX_ARROW_UP_RIGHT("bi-box-arrow-up-right")
    ;

}

@Composable
fun Icon(icon: Icon, attrs: AttrsScope<HTMLElement>.() -> Unit = {}, content: @Composable () -> Unit = {}) {
    I(attrs = {
        classes("bi", icon.cssClass)
        attrs(this)
    }) {
        content()
    }
}