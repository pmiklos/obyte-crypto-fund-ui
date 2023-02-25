package funddetails

import androidx.compose.runtime.Composable
import navigation.Navigator
import navigation.Screen
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun FundDetails(navigator: Navigator) {
    Text("Fund Address: ${navigator.param}")
    P {
        A(href = Screen.Home.href) {
            Text("back")
        }
    }
}