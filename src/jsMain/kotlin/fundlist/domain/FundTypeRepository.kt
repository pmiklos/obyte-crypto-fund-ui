package fundlist.domain

interface FundTypeRepository {

    suspend fun getFundTypes(): List<FundType>

}
