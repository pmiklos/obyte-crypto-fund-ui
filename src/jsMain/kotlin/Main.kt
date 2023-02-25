import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import navigation.NavHost
import navigation.Navigator
import navigation.Screen
import funddetails.FundDetails
import fundlist.FundList
import fundlist.FundListViewModel
import fundlist.FundType
import fundlist.FundTypeRepository
import fundlist.GetFundTypesUseCase
import fundlist.GetFundsUseCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import obyte.MockAddressDefinitionService
import obyte.MockAssetMetadataService
import obyte.MockBaseAgentService
import obyte.ObyteFundRepository
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.renderComposable
import wallet.WalletWidget

fun main() {
    Application().start()
}

class Application(override val coroutineContext: CoroutineContext = Job()) : CoroutineScope {

    fun start() = launch {
        val assetMetadataService = MockAssetMetadataService()
        val addressDefinitionService = MockAddressDefinitionService()
        val baseAgentService = MockBaseAgentService()

        val fundReleaseRepository = HardCodedTypeRepository
        val fundRepository =
            ObyteFundRepository(baseAgentService, addressDefinitionService, assetMetadataService)

        val getFundTypesUseCase = GetFundTypesUseCase(fundReleaseRepository)
        val getFundsUseCase = GetFundsUseCase(fundRepository)

        renderComposable(rootElementId = "root") {
            val walletAddress = remember { mutableStateOf("") }

            PageHeader(title = "Crypto Funds") {
                WalletWidget(walletAddress)
            }
            Main {
                val navigator = remember { Navigator(root = Screen.Home) }
                val fundListViewModel = FundListViewModel(getFundTypesUseCase, getFundsUseCase, navigator, this@launch)

                NavHost(navigator) {
                    composable(Screen.Home) {
                        FundList(fundListViewModel)
                    }

                    composable(Screen.Details) {
                        FundDetails(navigator)
                    }
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