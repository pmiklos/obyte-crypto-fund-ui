package ledger.obyte

import funddetails.domain.Asset
import funddetails.domain.AssetAllocation
import funddetails.domain.Balance
import funddetails.domain.FundDetails
import funddetails.domain.FundDetailsRepository

class ObyteFundDetailsRepository(
    private val autonomousAgentService: AutonomousAgentService,
    private val addressDefinitionService: AddressDefinitionService,
    private val assetMetadataService: AssetMetadataService,
    private val balanceService: BalanceService
) : FundDetailsRepository {
    override suspend fun getFundDetails(address: String): FundDetails {
        val state = autonomousAgentService.getState(address)
        val balances = balanceService.getBalances(listOf(address))
        val addressDefinition = addressDefinitionService.getDefinitionForAddress(address)
        val fundDefinition = addressDefinition.asFundDefinition()

        val shareAsset = state["asset"] as? String ?: throw RuntimeException("Fund not initialized")
        val shareAssetMetadata = assetMetadataService.getAssetMetadata(shareAsset)
        val sharesIssued = (state["total_shares"] as? Double)?.toLong() ?: 0L

        return FundDetails(
            address = address,
            totalShares = Balance(Asset(shareAsset, shareAssetMetadata.ticker, shareAssetMetadata.decimals), sharesIssued),
            allocation = fundDefinition.portfolio.map {
                val assetMetadata = assetMetadataService.getAssetMetadata(it.asset)

                AssetAllocation(
                    balance = Balance(
                        asset = Asset(
                            hash = it.asset,
                            name = assetMetadata.ticker,
                            decimals = assetMetadata.decimals
                        ),
                        amount = balances[address]?.get(it.asset)?.stable?: 0
                    ),
                    targetPercentage = it.percentage
                )
            }
        )
    }
}