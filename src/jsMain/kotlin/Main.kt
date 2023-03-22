import androidx.compose.runtime.mutableStateOf
import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.GetFundDetailsUseCase
import funddetails.view.FundDetails
import funddetails.view.FundDetailsViewModel
import fundlist.usecase.GetFundTypesUseCase
import fundlist.usecase.GetFundsUseCase
import fundlist.view.FundList
import fundlist.view.FundListViewModel
import ledger.obyte.ObyteBackend
import ledger.obyte.obytejs.ObyteJsApi
import ledger.obyte.obytejs.Testnet
import network.usecase.GetNetworkInfoUseCase
import network.view.NetworkInfo
import network.view.NetworkInfoViewModel
import navigation.NavHost
import navigation.Navigator
import navigation.Screen
import network.usecase.CreateFundShareIssuanceUriUseCase
import network.usecase.GetAddressExplorerUseCase
import network.usecase.GetAssetExplorerUseCase
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.renderComposable
import wallet.WalletWidget

fun main() {
    val obyteApi = ObyteJsApi(Testnet)
    val obyte = ObyteBackend(obyteApi)

    val fundTypeRepository = obyte.fundTypeRepository
    val fundListRepository = obyte.fundListRepository
    val fundDetailsRepository = obyte.fundDetailsRepository
    val connectionStatusRepository = obyte.connectionStatusRepository
    val explorerRepository = obyte.explorerRepository

    val getFundTypesUseCase = GetFundTypesUseCase(fundTypeRepository)
    val getFundsUseCase = GetFundsUseCase(fundListRepository)
    val getNetworkInfoUseCase = GetNetworkInfoUseCase(connectionStatusRepository)

    renderComposable(rootElementId = "root") {
        val navigator = Navigator(root = Screen.Home)
        val walletAddress = mutableStateOf("")
        val fundListViewModel = FundListViewModel(getFundTypesUseCase, getFundsUseCase)

        PageHeader(title = "Crypto Funds") {
            NetworkInfo(NetworkInfoViewModel(getNetworkInfoUseCase))
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
                            GetAddressExplorerUseCase(explorerRepository),
                            GetAssetExplorerUseCase(explorerRepository),
                            CreateFundShareIssuanceUriUseCase(connectionStatusRepository),
                            navigator
                        )
                    )
                }
            }
        }
    }
}