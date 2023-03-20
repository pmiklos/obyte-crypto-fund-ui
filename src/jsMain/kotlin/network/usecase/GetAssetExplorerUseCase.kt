package network.usecase

import network.domain.ExplorerRepository

class GetAssetExplorerUseCase(private val explorerRepository: ExplorerRepository) {
    operator fun invoke(address: String) = explorerRepository.getAssetUrl(address)
}