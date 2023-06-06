package navigation

sealed class Screen(override val route: String): Navigable {
    object Home : Screen("home") {
        val href = "#"
    }
    object Details: Screen("details")
    object Faq: Screen("faq") {
        val href = "#faq"
    }

    fun href(param: String) = "#$route/$param"

}
