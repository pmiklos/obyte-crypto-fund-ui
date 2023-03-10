package obyte

import fundlist.Fund
import fundlist.FundListRepository
import fundlist.FundType
import fundlist.Portfolio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObyteFundListRepository(
    private val baseAgentService: BaseAgentService,
    private val addressDefinitionService: AddressDefinitionService,
    private val assetMetadataService: AssetMetadataService
) : FundListRepository {

    override suspend fun getFunds(fundType: FundType): Flow<Fund> = flow {
        val funds = baseAgentService.getSubAgents(fundType.address)

        funds.forEach { fundAddress ->
            val addressDefinition = addressDefinitionService.getDefinitionForAddress(fundAddress)
            val fundDefinition = addressDefinition.asFundDefinition()
            emit(Fund(
                address = fundAddress,
                portfolio = fundDefinition.portfolio.map { item ->
                    Portfolio(
                        assetName = assetMetadataService.getAssetMetadata(item.asset).ticker,
                        percentage = item.percentage
                    )
                }
            ))
        }
    }

}