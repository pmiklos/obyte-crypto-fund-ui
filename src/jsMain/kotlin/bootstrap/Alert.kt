package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun Info(
    message: String
) {
    Div(attrs = { classes("alert", "alert-info") }) {
        Text(message)
    }
}

@Composable
fun Warning(
    message: String
) {
    Div(attrs = { classes("alert", "alert-warning") }) {
        Text(message)
    }
}