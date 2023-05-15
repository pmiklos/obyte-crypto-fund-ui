package network.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import bootstrap.ButtonOutlineInfo
import bootstrap.ButtonOutlineSecondary
import bootstrap.Icon
import common.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import network.usecase.GetNetworkInfoUseCase
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Composable
fun NetworkInfo(
    networkInfoViewModel: NetworkInfoViewModel
) {
    val networkInfo = networkInfoViewModel.state.value
    ButtonOutlineSecondary(attrs = {
        classes("dropdown-toggle")
        attr("data-bs-toggle", "dropdown")
        attr("aria-expanded", "false")
    }) {
        if (networkInfo.error.isNotBlank()) {
            Icon(Icon.BAR_CHART, attrs = {
                classes("text-danger")
            })
        } else {
            Icon(Icon.BAR_CHART_FILL)
        }
    }
    Ul(
        attrs = { classes("dropdown-menu", "dropdown-menu-end") }
    ) {
        networkInfo.links.forEach { (label, uri) ->
            Li {
                A(href = uri, attrs = { classes("dropdown-item") }) {
                    Text(label)
                }
            }
        }

        if (!networkInfo.links.isEmpty()) {
            Li {
                Hr(attrs = { classes("dropdown-divider") })
            }
        }

        Li(attrs = { classes("dropdown-item") }) {
            if (networkInfo.error.isNotBlank()) {
                Text(networkInfo.error)
            } else {
                Text(networkInfo.description)
                Br()
                Text("Connected to ${networkInfo.node}")
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
                            network = it.network.name,
                            description = it.network.description,
                            node = it.node,
                            links = it.network.links.associate { link ->
                                link.label to link.uri
                            }.toMap()
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }
}

data class NetworkInfoBean(
    val network: String = "-",
    val description: String = "",
    val node: String = "-",
    val error: String = "",
    val links: Map<String, String> = emptyMap()
)