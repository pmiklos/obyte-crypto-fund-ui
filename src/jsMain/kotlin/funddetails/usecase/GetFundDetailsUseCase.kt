package funddetails.usecase

import common.Resource
import funddetails.domain.FundDetails
import funddetails.domain.FundDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFundDetailsUseCase(
    private val fundDetailsRepository: FundDetailsRepository
) {

    operator fun invoke(address: String): Flow<Resource<FundDetails>> = flow {
        emit(Resource.Loading())

        try {
            val fundDetails = fundDetailsRepository.getFundDetails(address)
            emit(Resource.Success(fundDetails))
        } catch (e: Throwable) {
            console.error(e)
            emit(Resource.Error(e.message))
        }
    }
}
