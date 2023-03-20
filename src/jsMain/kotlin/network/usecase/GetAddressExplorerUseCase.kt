package network.usecase

import network.domain.ExplorerRepository

class GetAddressExplorerUseCase(private val explorerRepository: ExplorerRepository) {
    operator fun invoke(address: String) = explorerRepository.getAddressUrl(address)
}