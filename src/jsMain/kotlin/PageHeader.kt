import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Text

@Composable
fun PageHeader(
    title: String,
    controls: @Composable () -> Unit
) {
    Header(attrs = {
        classes("pb-3")
    }) {
        Div(
            attrs = { classes("d-flex", "flex-column", "flex-md-row") }
        ) {
            H4(
                attrs = { classes("d-flex") }
            ) {
                Text(title)
            }
            Div(
                attrs = {
                    classes("d-flex", "ms-md-auto", "w-auto")
                }
            ) {
                controls()
            }
        }
    }
}