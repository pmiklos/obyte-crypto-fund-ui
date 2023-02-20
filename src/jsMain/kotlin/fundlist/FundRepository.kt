package fundlist

import kotlinx.coroutines.flow.Flow

interface FundRepository {

    suspend fun getFunds(fundType: FundType): Flow<Fund>
}