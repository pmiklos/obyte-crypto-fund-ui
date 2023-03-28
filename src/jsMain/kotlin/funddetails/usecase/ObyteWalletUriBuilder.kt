package funddetails.usecase

import common.btoa
import common.encodeURIComponent
import network.domain.ConnectionStatusRepository

// TODO this should be abstracted away and have implementation per network
class ObyteWalletUriBuilder(private val connectionStatusRepository: ConnectionStatusRepository) {

    enum class Intent {
        ISSUE,
        REDEEM
    }

    private val protocol get() = when (connectionStatusRepository.getConnectionStatus().network.name) {
        "testnet" -> "obyte-tn"
        "livenet" -> "obyte"
        else -> ""
    }

    operator fun invoke(
        address: String,
        fee: Long = 10_000L,
        intent: Intent,
        assetPayments: Map<String, Long> = emptyMap(),
        fromAddress: String? = null
    ): String {
        val data = when(intent) {
            Intent.ISSUE -> """{"intent":"issue"}"""
            Intent.REDEEM -> """{"intent":"redeem"}"""
        }
        val uri = StringBuilder("$protocol:$address?amount=$fee")
        uri.append("&base64data=").append(encodeURIComponent(btoa(data)))
        assetPayments.entries.forEachIndexed { index, (asset, amount) ->
            uri.append("&asset${index + 2}=${encodeURIComponent(asset)}")
            uri.append("&amount${index + 2}=$amount")
        }
        fromAddress?.let {
            if (it.isNotBlank()) {
                uri.append("&from_address=$it")
            }
        }
        return uri.toString()
    }

}