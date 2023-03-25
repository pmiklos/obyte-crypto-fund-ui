package ledger.obyte

import fundlist.domain.Fund
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.Portfolio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObyteFundListRepository(
    private val baseAgentService: BaseAgentService,
    private val assetMetadataService: AssetMetadataService,
    private val autonomousAgentService: AutonomousAgentService,
) : FundListRepository {

    override suspend fun getFunds(fundType: FundType): Flow<Fund> = flow {
        val subAgents = baseAgentService.getSubAgents(fundType.address)

        subAgents.forEach { subAgent ->
            val fundDefinition = subAgent.definition.asFundDefinition()
            val state = autonomousAgentService.getState(subAgent.address)
            val asset = state["asset"] as String?
            val assetName = asset?.run {
                assetMetadataService.getAssetMetadata(asset).ticker
            }?: "Uninitialized"

            emit(Fund(
                address = subAgent.address,
                assetName = assetName,
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