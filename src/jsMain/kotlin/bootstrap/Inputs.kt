package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.dom.Input

@Composable
fun TextInput(attrs: InputAttrsScope<String>.() -> Unit) {
    Input(type = InputType.Text, attrs = {
        classes("form-control")
        attrs.invoke(this)
    })
}
