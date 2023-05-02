package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.window

@Composable
fun NavHost(navigator: Navigator, navigations: NavHostBuilder.() -> Unit) {
    remember {
        window.onhashchange = { event ->
            navigator.updateRoute(event.newURL)
        }
    }

    val builder = remember { NavHostBuilder() }
    navigations(builder)

    builder[navigator.route]?.let {
        it.onActivated(navigator.param)
        it.composable.invoke()
    }
}

class NavHostBuilder {

    data class RouteConfiguration(val onActivated: (String?) -> Unit = {}, val composable: @Composable () -> Unit)

    private val routes = mutableMapOf<String, RouteConfiguration>()

    fun composable(screen: Navigable, onActivated: (String?) -> Unit = {}, composable: @Composable () -> Unit) {
        routes[screen.route] = RouteConfiguration(onActivated, composable)
    }

    operator fun get(route: String) = routes[route]
}

