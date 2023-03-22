package network.domain

interface ConnectionStatusRepository {
    fun getConnectionStatus(): ConnectionStatus
}
