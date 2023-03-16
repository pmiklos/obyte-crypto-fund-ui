package fundlist.usecase

import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository

class GetFundTypesUseCase(private val fundTypeRepository: FundTypeRepository) {

    suspend operator fun invoke(): List<FundType> {
        return fundTypeRepository.getFundTypes()
    }
}