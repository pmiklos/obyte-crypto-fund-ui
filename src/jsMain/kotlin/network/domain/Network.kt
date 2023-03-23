package network.domain

data class Network(
    val name: String,
    val description: String,
    val links: List<Link> = emptyList()
)

data class Link(
    val label: String,
    val uri: String
)
