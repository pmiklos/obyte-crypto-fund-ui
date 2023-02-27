package funddetails

interface FundDetailsRepository {

    suspend fun getFundDetails(address: String): FundDetails

}
