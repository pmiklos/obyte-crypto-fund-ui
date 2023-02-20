package fundlist

class GetFundTypesUseCase(private val fundTypeRepository: FundTypeRepository) {

    suspend operator fun invoke(): List<FundType> {
        return fundTypeRepository.getFundTypes()
    }
}