package ledger.obyte

import funddetails.domain.FundDetailsRepository
import fundlist.domain.FundListRepository
import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository
import network.domain.ConnectionStatusRepository
import network.domain.ExplorerRepository
import wallet.domain.WalletValidation
import wallet.domain.WalletValidationResult

class ObyteBackend(obyteApi: ObyteApi) {
    val fundTypeRepository: FundTypeRepository = object : FundTypeRepository {
        override suspend fun getFundTypes(): List<FundType> = listOf(
            FundType(
                address = "FTKLXQND4LS65OTQK7RXOABB7DENBSEI",
                description = "Two-asset fixed allocation fund",
                version = "0.0.1"
            ),
            FundType(
                address = "IQ7OZ2J2Y76BXO7GRESDN7BS2WDH5J67",
                description = "Two-asset fixed allocation fund",
                version = "0.0.2"
            )
        )
    }

    val fundListRepository: FundListRepository = ObyteFundListRepository(
        baseAgentService = obyteApi,
        assetMetadataService = obyteApi,
        autonomousAgentService = obyteApi
    )

    val fundDetailsRepository: FundDetailsRepository = ObyteFundDetailsRepository(
        autonomousAgentService = obyteApi,
        addressDefinitionService = obyteApi,
        assetMetadataService = obyteApi,
        balanceService = obyteApi
    )

    val connectionStatusRepository: ConnectionStatusRepository = ObyteConnectionStatusRepository(obyteApi)

    val explorerRepository: ExplorerRepository = object : ExplorerRepository {
        override fun getAddressUrl(address: String) = obyteApi.explorerUrl(address)
        override fun getAssetUrl(asset: String) = obyteApi.assetExplorerUrl(asset)
    }

    val walletValidation: WalletValidation = object : WalletValidation {
        override fun validateWalletAddress(address: String): WalletValidationResult =
            when (val result = obyteApi.validateAddress(address)) {
                is ValidationService.ValidateAddressResult.Valid -> WalletValidationResult.Valid
                is ValidationService.ValidateAddressResult.Invalid -> WalletValidationResult.Invalid(result.validationError)
            }
    }
}