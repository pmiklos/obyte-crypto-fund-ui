package fundlist.domain

interface FundListRepository {

    suspend fun getFunds(fundType: FundType): List<String>

    suspend fun getFundSummary(address: String): Fund?
}