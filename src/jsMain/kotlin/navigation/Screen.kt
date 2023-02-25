package navigation

sealed class Screen(override val route: String): Navigable {
    object Home : Screen("home") {
        val href = "#"
    }
    object Details: Screen("details")

    fun href(param: String) = "#$route/$param"
}
