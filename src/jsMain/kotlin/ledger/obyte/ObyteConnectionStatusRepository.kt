package ledger.obyte

import network.domain.ConnectionStatus
import network.domain.ConnectionStatusRepository
import network.domain.Link
import network.domain.Network

class ObyteConnectionStatusRepository(private val configurationService: ConfigurationService) :
    ConnectionStatusRepository {
    override fun getConnectionStatus() = ConnectionStatus(
        network = when (configurationService.network) {
            "testnet" -> Network(
                name = "testnet",
                description = "Obyte testnet",
                links = listOf(
                    Link("Download wallet", "https://obyte.org/testnet")
                )
            )

            "livenet" -> Network(
                name = "livenet",
                description = "Obyte livenet",
                links = listOf(
                    Link("Download wallet", "https://obyte.org/#download")
                )
            )

            else -> Network(configurationService.network, "Unknown Obyte network")
        },
        node = configurationService.node
    )
}