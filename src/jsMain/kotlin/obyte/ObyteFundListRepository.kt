package obyte

import fundlist.domain.Fund
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.Portfolio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObyteFundListRepository(
    private val baseAgentService: BaseAgentService,
    private val assetMetadataService: AssetMetadataService
) : FundListRepository {

    override suspend fun getFunds(fundType: FundType): Flow<Fund> = flow {
        val subAgents = baseAgentService.getSubAgents(fundType.address)

        subAgents.forEach { subAgent ->
            val fundDefinition = subAgent.definition.asFundDefinition()
            emit(Fund(
                address = subAgent.address,
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