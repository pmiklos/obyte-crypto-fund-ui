package funddetails.usecase

class CreateAssetRedemptionUriUseCase(private val uriBuilder: ObyteWalletUriBuilder) {

    operator fun invoke(
        address: String,
        fee: Long = 10_000L,
        shareAsset: String,
        shareAmount: Long
    ) = uriBuilder.invoke(
        address,
        fee,
        ObyteWalletUriBuilder.Intent.REDEEM,
        mapOf(shareAsset to shareAmount)
    )
}