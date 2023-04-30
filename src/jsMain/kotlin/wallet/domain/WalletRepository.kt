package wallet.domain

interface WalletRepository {

    fun setWallet(address: String)

    fun getWallet(): String

    fun removeWallet()

}