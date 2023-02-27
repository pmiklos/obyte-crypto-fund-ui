package obyte

import funddetails.Asset
import funddetails.AssetAllocation
import funddetails.Balance
import funddetails.FundDetails
import funddetails.FundDetailsRepository

class ObyteFundDetailsRepository(balanceService: BalanceService) : FundDetailsRepository {
    override suspend fun getFundDetails(address: String): FundDetails {
        return FundDetails(
            address = address,
            totalShares = Balance(Asset("assetHash", "FUND-1", 0), 30573801),
            allocation = listOf(
                AssetAllocation(
                    balance = Balance(Asset("vApNsebTEPb3QDNNfyLsDB/iI5st9koMpAqvADzTw5A=", "BTC", 8), 220279710),
                    targetPercentage = 0.146814334
                ),
                AssetAllocation(
                    balance = Balance(Asset("RF/ysZ/ZY4leyc3huUq1yFc0xTS0GdeFQu8RmXas4ys=", "ETH", 6), 12797202),
                    targetPercentage = 0.85321787464786
                )
            )
        )
    }
}