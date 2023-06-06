import androidx.compose.runtime.Composable
import bootstrap.NavBar
import bootstrap.NavBarBrand
import bootstrap.NavBarCollapse
import bootstrap.NavBarNav
import bootstrap.NavBarNavItem
import bootstrap.NavBarNavLink
import bootstrap.NavBarToggler
import navigation.Screen
import org.jetbrains.compose.web.dom.Div
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
        NavBar {
            NavBarBrand {
                Text(title)
            }
            NavBarToggler()
            NavBarCollapse {
                NavBarNav {
                    NavBarNavItem {
                        NavBarNavLink(Screen.Home.href) {
                            Text("Home")
                        }
                    }
                    NavBarNavItem {
                        NavBarNavLink(Screen.Faq.href) {
                            Text("F.A.Q")
                        }
                    }
                }
            }
            Div (attrs = {classes("d-flex", "pt-3")}) {
                controls()
            }
        }
    }
}
