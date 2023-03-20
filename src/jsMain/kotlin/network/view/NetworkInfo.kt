package network.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import bootstrap.ButtonOutlineInfo
import common.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import network.usecase.GetNetworkInfoUseCase
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Composable
fun NetworkInfo(
    networkInfoViewModel: NetworkInfoViewModel
) {
    val networkInfo = networkInfoViewModel.state.value
    Div(attrs = { classes("btn-group") }) {
        ButtonOutlineInfo(attrs = {
            classes("mb-3", "me-1", "dropdown-toggle")
            attr("data-bs-toggle", "dropdown")
            attr("aria-expanded", "false")
        }) {
            Text(networkInfo.network)
        }
        Ul(
            attrs = { classes("dropdown-menu") }
        ) {
            Li(attrs = { classes("dropdown-item") }) {
                if (networkInfo.error.isNotBlank()) {
                    Text(networkInfo.error)
                } else {
                    Text("Connected to ${networkInfo.node}")
                }
            }
        }
    }
}

class NetworkInfoViewModel(
    private val getNetworkInfo: GetNetworkInfoUseCase
) {
    private val _state = mutableStateOf(NetworkInfoBean())
    val state: State<NetworkInfoBean> = _state

    init {
        CoroutineScope(Job()).launch {
            updateNetworkInfo(this)
        }.start()
    }

    private fun updateNetworkInfo(coroutineScope: CoroutineScope) {
        getNetworkInfo()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> _state.value = NetworkInfoBean("...")
                    is Resource.Error -> _state.value =
                        NetworkInfoBean(
                            network = "Not connected",
                            error = result.message ?: "Unknown error occured"
                        )

                    is Resource.Success -> result.data?.let {
                        _state.value = NetworkInfoBean(
                            network = it.network,
                            node = it.node
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }
}

data class NetworkInfoBean(
    val network: String = "-",
    val node: String = "-",
    val error: String = ""
)