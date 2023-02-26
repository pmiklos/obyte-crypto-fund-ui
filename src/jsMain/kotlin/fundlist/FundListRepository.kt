package fundlist

import kotlinx.coroutines.flow.Flow

interface FundListRepository {

    suspend fun getFunds(fundType: FundType): Flow<Fund>
}