package network.domain

interface ConnectionStatusRepository {
    suspend fun getConnectionStatus(): ConnectionStatus
}
