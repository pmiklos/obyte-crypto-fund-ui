package funddetails.usecase

class CreateFundShareIssuanceUriUseCase(private val uriBuilder: ObyteWalletUriBuilder) {

    operator fun invoke(
        address: String,
        fromAddress: String,
        fee: Long = 10_000L,
        assetPayments: Map<String, Long> = emptyMap()
    ) = uriBuilder.invoke(
        address,
        fee,
        ObyteWalletUriBuilder.Intent.ISSUE,
        assetPayments,
        fromAddress
    )
}