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
import wallet.domain.WalletRepository
import wallet.domain.WalletValidationResult
import wallet.usecase.ValidateWalletAddressUseCase

@Composable
fun WalletWidget(
    wallet: WalletBean,
    onAddressChanged: (String) -> Unit = {},
    attrs: AttrsScope<HTMLDivElement>.() -> Unit = {},
    addOns: @Composable () -> Unit = {}
) {
    val border = when (wallet.validation) {
        is WalletValidationResult.Empty -> "border-warning"
        is WalletValidationResult.Invalid -> "border-danger"
        is WalletValidationResult.Valid -> "border-secondary-subtle"
    }

    InputGroup(
        attrs = attrs
    ) {
        AddOn("Wallet")
        Input(
            type = InputType.Text
        ) {
            classes("form-control", border)
            placeholder("Enter your wallet address")
            value(wallet.address)
            onInput { event -> onAddressChanged(event.value) }
            maxLength(32)
            title(wallet.validation.description)
        }
        addOns()
    }
}

data class WalletBean(
    val address: String = "",
    val validation: WalletValidationResult = WalletValidationResult.Empty
)

class WalletModel(
    private val walletRepository: WalletRepository,
    private val validateWalletAddress: ValidateWalletAddressUseCase
) {
    private var listener: (String) -> Unit = {}
    private val _state = mutableStateOf(WalletBean())
    val state: State<WalletBean> = _state

    init {
        updateAddress(walletRepository.getWallet())
    }

    fun updateAddress(address: String) {
        val validationResult = validateWalletAddress(address)
        _state.value = _state.value.copy(
            address = address,
            validation = validationResult
        )

        if (validationResult == WalletValidationResult.Valid) {
            walletRepository.setWallet(address)
            listener.invoke(address)
        } else {
            walletRepository.removeWallet()
            listener.invoke("")
        }
    }

    fun onAddressChanged(listener: (String) -> Unit) {
        this.listener = listener
    }
}