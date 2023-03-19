package ledger.obyte

import funddetails.domain.FundDetailsRepository
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository

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
}