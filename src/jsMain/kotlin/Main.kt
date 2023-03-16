import androidx.compose.runtime.mutableStateOf
import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.GetFundDetailsUseCase
import funddetails.view.FundDetails
import funddetails.view.FundDetailsViewModel
import fundlist.view.FundList
import fundlist.view.FundListViewModel
import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository
import fundlist.usecase.GetFundTypesUseCase
import fundlist.usecase.GetFundsUseCase
import navigation.NavHost
import navigation.Navigator
import navigation.Screen
import obyte.MockAddressDefinitionService
import obyte.MockAssetMetadataService
import obyte.MockAutonomousAgentService
import obyte.MockBalanceService
import obyte.MockBaseAgentService
import obyte.ObyteFundDetailsRepository
import obyte.ObyteFundListRepository
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.renderComposable
import wallet.WalletWidget

fun main() {
    val autonomousAgentService = MockAutonomousAgentService
    val assetMetadataService = MockAssetMetadataService
    val addressDefinitionService = MockAddressDefinitionService
    val baseAgentService = MockBaseAgentService
    val balanceService = MockBalanceService

    val fundTypeRepository = HardCodedTypeRepository
    val fundListRepository =
        ObyteFundListRepository(baseAgentService, addressDefinitionService, assetMetadataService)

    val getFundTypesUseCase = GetFundTypesUseCase(fundTypeRepository)
    val getFundsUseCase = GetFundsUseCase(fundListRepository)

    val fundDetailsRepository =
        ObyteFundDetailsRepository(
            autonomousAgentService,
            addressDefinitionService,
            assetMetadataService,
            balanceService
        )

    renderComposable(rootElementId = "root") {
        val navigator = Navigator(root = Screen.Home)
        val walletAddress = mutableStateOf("")
        val fundListViewModel = FundListViewModel(getFundTypesUseCase, getFundsUseCase)

        PageHeader(title = "Crypto Funds") {
            WalletWidget(walletAddress)
        }
        Main {
            NavHost(navigator) {
                composable(Screen.Home) {
                    FundList(fundListViewModel)
                }
                composable(Screen.Details) {
                    FundDetails(
                        FundDetailsViewModel(
                            GetFundDetailsUseCase(fundDetailsRepository),
                            CalculateAssetPaymentUseCase,
                            navigator
                        )
                    )
                }
            }
        }
    }
}

object HardCodedTypeRepository : FundTypeRepository {
    override suspend fun getFundTypes(): List<FundType> = listOf(
        FundType(
            address = "YSOBOFK4AVXHYV2GC7MVZMOYCNKP52TX",
            description = "Two-asset fixed allocation fund",
            version = "0.0.1"
        )
    )

}