package network.usecase

import common.btoa
import common.encodeURIComponent
import network.domain.ConnectionStatusRepository

class CreateFundShareIssuanceUriUseCase(private val connectionStatusRepository: ConnectionStatusRepository) {

    // TODO these are all Obyte specific, this should not be in the use-case
    private val protocol get() = when (connectionStatusRepository.getConnectionStatus().network.name) {
        "testnet" -> "obyte-tn"
        "livenet" -> "obyte"
        else -> ""
    }

    operator fun invoke(
        address: String,
        amount: Long = 10_000L,
        assets: Map<String, Long> = emptyMap()
    ): String {
        val uri = StringBuilder("$protocol:$address?amount=$amount")
        uri.append("&base64data=").append(encodeURIComponent(btoa("""{"intent":"issue"}""")))
        assets.entries.forEachIndexed { index, (asset, amount) ->
            uri.append("&asset${index + 2}=${encodeURIComponent(asset)}")
            uri.append("&amount${index + 2}=$amount")
        }
        return uri.toString()
    }
}