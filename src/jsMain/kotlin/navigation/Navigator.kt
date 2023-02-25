package navigation

import androidx.compose.runtime.mutableStateOf
import kotlinx.browser.window

class Navigator(private val root: Navigable) {

    private data class Route(val name: String, val param: String?)

    private val _route = mutableStateOf(routeOf(window.location.href, root.route))

    val route get() = _route.value.name
    val param get() = _route.value.param

    fun updateRoute(url: String) {
        _route.value = routeOf(url, root.route)
    }

    private fun routeOf(url: String, defaultRoute: String): Route {
        val path = url.substringAfter("#", "").split("/")
        val route = if (path.first().isEmpty()) defaultRoute else path.first()
        val param = if (path.size > 1) path[1] else null
        return Route(route, param)
    }
}

