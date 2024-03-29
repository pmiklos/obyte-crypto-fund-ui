import browser.LocalStorageRepository
import faq.view.Faq
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
import ledger.obyte.cached
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
    val obyte = ObyteBackend(obyteApi.cached())

    val fundTypeRepository = obyte.fundTypeRepository
    val fundListRepository = obyte.fundListRepository
    val fundDetailsRepository = obyte.fundDetailsRepository
    val connectionStatusRepository = obyte.connectionStatusRepository
    val explorerRepository = obyte.explorerRepository
    val walletUriBuilder = ObyteWalletUriBuilder(connectionStatusRepository)

    val getFundTypesUseCase = GetFundTypesUseCase(fundTypeRepository)
    val getFundsUseCase = GetFundsUseCase(fundListRepository)
    val getNetworkInfoUseCase = GetNetworkInfoUseCase(connectionStatusRepository)

    val navigator = Navigator(root = Screen.Home)
    val walletModel = WalletModel(LocalStorageRepository, ValidateWalletAddressUseCase(obyte.walletValidation))
    val fundListViewModel = FundListViewModel(getFundTypesUseCase, getFundsUseCase)
    val networkInfoViewModel = NetworkInfoViewModel(getNetworkInfoUseCase)
    val fundDetailsViewModel = FundDetailsViewModel(
        GetFundDetailsUseCase(fundDetailsRepository),
        CalculateAssetPaymentUseCase,
        GetAddressExplorerUseCase(explorerRepository),
        GetAssetExplorerUseCase(explorerRepository),
        CreateFundShareIssuanceUriUseCase(walletUriBuilder),
        CreateAssetRedemptionUriUseCase(walletUriBuilder),
        LocalStorageRepository
    )

    walletModel.onAddressChanged { address ->
        fundDetailsViewModel.updateWalletAddress(address)
    }

    renderComposable(rootElementId = "root") {
        PageHeader(title = "Crypto Funds") {
            WalletWidget(
                wallet = walletModel.state.value,
                onAddressChanged = walletModel::updateAddress
            ) {
                NetworkInfo(networkInfoViewModel)
            }
        }
        Main {
            NavHost(navigator) {
                composable(screen = Screen.Home) {
                    FundList(fundListViewModel)
                }
                composable(screen = Screen.Details, onActivated = fundDetailsViewModel::loadFund) {
                    FundDetails(fundDetailsViewModel)
                }
                composable(screen = Screen.Faq) {
                    Faq()
                }
            }
        }
    }
}