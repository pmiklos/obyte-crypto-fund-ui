package ledger.obyte

import fundlist.domain.Fund
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.Portfolio

class ObyteFundListRepository(
    private val baseAgentService: BaseAgentService,
    private val assetMetadataService: AssetMetadataService,
    private val autonomousAgentService: AutonomousAgentService,
) : FundListRepository {

    private val subAgentCache = mutableMapOf<String, SubAgent>()

    override suspend fun getFunds(fundType: FundType): List<String> {
        val subAgents = baseAgentService.getSubAgents(fundType.address)

        subAgents.forEach {
            subAgentCache[it.address] = it
        }

        return subAgents.map { subAgent ->
            subAgent.address
        }
    }

    override suspend fun getFundSummary(address: String): Fund? {
        val subAgent = subAgentCache[address] ?: return null

        val state = autonomousAgentService.getState(subAgent.address)
        val asset = state["asset"] as String?

        if (asset == null) {
            console.log("INFO: Found uninitialized fund: ${subAgent.address}")
            return null
        }

        val fundDefinition = subAgent.definition.asFundDefinition()
        val assetMetadata = assetMetadataService.getAssetMetadata(asset)
        return Fund(
            address = subAgent.address,
            assetName = assetMetadata.ticker,
            description = assetMetadata.description,
            portfolio = fundDefinition.portfolio.map { item ->
                Portfolio(
                    assetName = assetMetadataService.getAssetMetadata(item.asset).ticker,
                    percentage = item.percentage
                )
            }
        )
    }

}