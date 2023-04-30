package browser

import kotlinx.browser.localStorage
import wallet.domain.WalletRepository

private const val KEY_WALLET = "wallet"

object LocalStorageRepository : WalletRepository {

    override fun setWallet(address: String) = localStorage.setItem(KEY_WALLET, address)
    override fun getWallet() = localStorage.getItem(KEY_WALLET) ?: ""
    override fun removeWallet() = localStorage.removeItem(KEY_WALLET)

}