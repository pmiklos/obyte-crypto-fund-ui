package ledger.obyte

import funddetails.domain.FundDetailsRepository
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository
import network.domain.ConnectionStatus
import network.domain.ConnectionStatusRepository
import network.domain.ExplorerRepository

class ObyteBackend(obyteApi: ObyteApi) {
    val fundTypeRepository: FundTypeRepository = object : FundTypeRepository {
        override suspend fun getFundTypes(): List<FundType> = listOf(
            FundType(
                address = "FTKLXQND4LS65OTQK7RXOABB7DENBSEI",
                description = "Two-asset fixed allocation fund",
                version = "0.0.1"
            )
        )
    }

    val fundListRepository: FundListRepository = ObyteFundListRepository(
        baseAgentService = obyteApi,
        assetMetadataService = obyteApi
    )

    val fundDetailsRepository: FundDetailsRepository = ObyteFundDetailsRepository(
        autonomousAgentService = obyteApi,
        addressDefinitionService = obyteApi,
        assetMetadataService = obyteApi,
        balanceService = obyteApi
    )

    val connectionStatusRepository: ConnectionStatusRepository = object: ConnectionStatusRepository {
        override fun getConnectionStatus() = ConnectionStatus(
            network = obyteApi.network,
            node = obyteApi.node
        )
    }

    val explorerRepository: ExplorerRepository = object : ExplorerRepository {
        override fun getAddressUrl(address: String) = obyteApi.explorerUrl(address)
        override fun getAssetUrl(asset: String) = obyteApi.explorerUrl(asset)
    }
}