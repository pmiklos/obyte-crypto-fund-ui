import funddetails.usecase.CalculateAssetPaymentUseCase
import funddetails.usecase.CreateAssetRedemptionUriUseCase
import funddetails.usecase.CreateFundShareIssuanceUriUseCase
import funddetails.usecase.GetFundDetailsUseCase
import funddetails.usecase.ObyteWalletUriBuilder
import funddetails.view.FundDetails
import funddetails.view.FundDetailsViewModel
import fundlist.usecase.GetFundTypesUseCase
import fundlist.usecase.GetFundsUseCase
import fundlist.view.FundList
import fundlist.view.FundListViewModel
import ledger.obyte.ObyteBackend
import ledger.obyte.obytejs.ObyteJsApi
import ledger.obyte.obytejs.Testnet
import navigation.NavHost
import navigation.Navigator
import navigation.Screen
import network.usecase.GetAddressExplorerUseCase
import network.usecase.GetAssetExplorerUseCase
import network.usecase.GetNetworkInfoUseCase
import network.view.NetworkInfo
import network.view.NetworkInfoViewModel
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.renderComposable
import wallet.usecase.ValidateWalletAddressUseCase
import wallet.view.WalletModel
import wallet.view.WalletWidget

fun main() {
    val obyteApi = ObyteJsApi(Testnet)
    val obyte = ObyteBackend(obyteApi)

    val fundTypeRepository = obyte.fundTypeRepository
    val fundListRepository = obyte.fundListRepository
    val fundDetailsRepository = obyte.fundDetailsRepository
    val connectionStatusRepository = obyte.connectionStatusRepository
    val explorerRepository = obyte.explorerRepository
    val walletUriBuilder = ObyteWalletUriBuilder(connectionStatusRepository)

    val getFundTypesUseCase = GetFundTypesUseCase(fundTypeRepository)
    val getFundsUseCase = GetFundsUseCase(fundListRepository)
    val getNetworkInfoUseCase = GetNetworkInfoUseCase(connectionStatusRepository)

    renderComposable(rootElementId = "root") {
        val navigator = Navigator(root = Screen.Home)
        val fundListViewModel = FundListViewModel(getFundTypesUseCase, getFundsUseCase)
        val walletModel = WalletModel(ValidateWalletAddressUseCase(obyte.walletValidation))

        PageHeader(title = "Crypto Funds") {
            NetworkInfo(NetworkInfoViewModel(getNetworkInfoUseCase))
            WalletWidget(
                wallet = walletModel.state.value,
                onAddressChanged = walletModel::updateAddress
            )
        }
        Main {
            NavHost(navigator) {
                composable(Screen.Home) {
                    FundList(fundListViewModel)
                }
                composable(Screen.Details) {
                    val fundDetailsViewModel = FundDetailsViewModel(
                        GetFundDetailsUseCase(fundDetailsRepository),
                        CalculateAssetPaymentUseCase,
                        GetAddressExplorerUseCase(explorerRepository),
                        GetAssetExplorerUseCase(explorerRepository),
                        CreateFundShareIssuanceUriUseCase(walletUriBuilder),
                        CreateAssetRedemptionUriUseCase(walletUriBuilder),
                        navigator
                    )
                    // TODO how to handle interaction between global walletModel and fundDetailsViewModel?
                    walletModel.onAddressChanged { address ->
                        fundDetailsViewModel.updateWalletAddress(address)
                    }
                    FundDetails(fundDetailsViewModel)
                }
            }
        }
    }
}