package funddetails

import common.Resource
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
            console.log(e.message)
            emit(Resource.Error(e.message))
        }
    }
}
