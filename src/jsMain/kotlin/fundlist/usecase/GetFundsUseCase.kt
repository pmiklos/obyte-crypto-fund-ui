package fundlist.usecase

import common.Resource
import fundlist.domain.Fund
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFundsUseCase(
    private val fundListRepository: FundListRepository
) {

    operator fun invoke(fundType: FundType): Flow<Resource<Fund>> = flow {
        val funds = fundListRepository.getFunds(fundType)

        funds.forEach { address ->
            emit(Resource.Loading(Fund(address)))
        }

        funds.forEach { address ->
            fundListRepository.getFundSummary(address)?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error("Not initialized", Fund(address)))
        }
    }
}