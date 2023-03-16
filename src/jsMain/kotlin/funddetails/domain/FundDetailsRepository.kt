package funddetails.domain

interface FundDetailsRepository {

    suspend fun getFundDetails(address: String): FundDetails

}
