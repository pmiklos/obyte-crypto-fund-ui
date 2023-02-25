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
    builder[navigator.route]?.invoke()
}

class NavHostBuilder {
    private val composables = mutableMapOf<String, @Composable () -> Unit>()

    fun composable(navigable: Navigable, composable: @Composable () -> Unit) {
        composables[navigable.route] = composable
    }

    operator fun get(route: String) = composables[route]
}

