package wallet.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import bootstrap.AddOn
import bootstrap.InputGroup
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.maxLength
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLDivElement
import wallet.domain.WalletValidationResult
import wallet.usecase.ValidateWalletAddressUseCase

@Composable
fun WalletWidget(
    model: WalletModel,
    attrs: AttrsScope<HTMLDivElement>.() -> Unit = {}
) {
    val wallet = model.state.value

    InputGroup(
        attrs = attrs
    ) {
        AddOn("Wallet")
        Input(
            type = InputType.Text
        ) {
            classes("form-control")
            placeholder("Enter your wallet address")
            value(wallet.address)
            onInput { event -> model.updateAddress(event.value) }
            maxLength(32)
        }
        AddOn(
            text = when (wallet.validation) {
                is WalletValidationResult.Empty -> "⚫"
                is WalletValidationResult.Invalid -> "🔴"
                is WalletValidationResult.Valid -> "🟢"
            },
            attrs = {
                title(wallet.validation.description)
            }
        )
    }
}

data class WalletBean(
    val address: String = "",
    val validation: WalletValidationResult = WalletValidationResult.Empty
)

class WalletModel(
    private val validateWalletAddress: ValidateWalletAddressUseCase
) {
    private val _state = mutableStateOf(WalletBean())
    private val _listener = mutableStateOf<(String) -> Unit>({ _ -> })
    val state: State<WalletBean> = _state

    fun updateAddress(address: String) {
        val validationResult = validateWalletAddress(address)
        _state.value = _state.value.copy(
            address = address,
            validation = validationResult
        )
        _listener.value.invoke(
            when (validationResult) {
                WalletValidationResult.Valid -> address
                else -> ""
            }
        )
    }

    fun onAddressChanged(listener: (String) -> Unit) {
        _listener.value = listener
    }
}