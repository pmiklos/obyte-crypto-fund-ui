package fundlist

import common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class GetFundsUseCase(
    private val fundListRepository: FundListRepository
) {

    operator fun invoke(fundType: FundType): Flow<Resource<List<Fund>>> = flow {
        emit(Resource.Loading())

        fundListRepository
            .getFunds(fundType)
            .onEach {
                emit(Resource.Success(listOf(it)))
            }
            .catch {
                emit(Resource.Error(it.message))
            }
            .collect()
    }
}