package network.usecase

import common.Resource
import kotlinx.coroutines.flow.flow
import network.domain.ConnectionStatusRepository

class GetNetworkInfoUseCase(private val connectionStatusRepository: ConnectionStatusRepository) {
    operator fun invoke() = flow {
        emit(Resource.Loading())

        try {
            val connectionStatus = connectionStatusRepository.getConnectionStatus()
            emit(Resource.Success(connectionStatus))
        } catch (e: Exception) {
            console.error(e)
            emit(Resource.Error(e.message))
        }
    }
}