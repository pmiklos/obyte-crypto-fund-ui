package network.domain

interface ExplorerRepository {
    fun getAddressUrl(address: String): String
    fun getAssetUrl(asset: String): String
}