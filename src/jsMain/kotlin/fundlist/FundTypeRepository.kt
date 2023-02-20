package fundlist

interface FundTypeRepository {

    suspend fun getFundTypes(): List<FundType>

}
