import androidx.compose.runtime.mutableStateOf
import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.GetFundDetailsUseCase
import funddetails.view.FundDetails
import funddetails.view.FundDetailsViewModel
import fundlist.domain.FundType
import fundlist.domain.FundTypeRepository
import fundlist.usecase.GetFundTypesUseCase
import fundlist.usecase.GetFundsUseCase
import fundlist.view.FundList
import fundlist.view.FundListViewModel
import navigation.NavHost
import navigation.Navigator
import navigation.Screen
import obyte.MockObyteApi
import obyte.ObyteFundDetailsRepository
import obyte.ObyteFundListRepository
import obyte.ObyteJsApi
import obyte.Testnet
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.renderComposable
import wallet.WalletWidget

fun main() {
    val obyteApi = ObyteJsApi(Testnet)

    val fundTypeRepository = HardCodedTypeRepository
    val fundListRepository = ObyteFundListRepository(
        baseAgentService = obyteApi,
        assetMetadataService = obyteApi
    )

    val getFundTypesUseCase = GetFundTypesUseCase(fundTypeRepository)
    val getFundsUseCase = GetFundsUseCase(fundListRepository)

    val fundDetailsRepository = ObyteFundDetailsRepository(
        autonomousAgentService = obyteApi,
        addressDefinitionService = obyteApi,
        assetMetadataService = obyteApi,
        balanceService = obyteApi
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
            address = "FTKLXQND4LS65OTQK7RXOABB7DENBSEI",
            description = "Two-asset fixed allocation fund",
            version = "0.0.1"
        )
    )

}