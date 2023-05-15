import androidx.compose.runtime.Composable
import bootstrap.FlexResponsive
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
        FlexResponsive(
            left = {
                H4 {
                    Text(title)
                }
            },
            right = {
                controls()
            }
        )
    }
}