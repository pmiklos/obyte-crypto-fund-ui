package browser

import kotlinx.browser.localStorage

private const val KEY_WALLET = "wallet"

object LocalStorageRepository :
    wallet.domain.WalletRepository,
    funddetails.domain.WalletRepository {

    override fun setWallet(address: String) = localStorage.setItem(KEY_WALLET, address)
    override fun getWallet() = localStorage.getItem(KEY_WALLET) ?: ""
    override fun removeWallet() = localStorage.removeItem(KEY_WALLET)

}