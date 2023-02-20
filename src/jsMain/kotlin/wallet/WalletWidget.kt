package wallet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import bootstrap.AddOn
import bootstrap.InputGroup
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.maxLength
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLDivElement

@Composable
fun WalletWidget(
    walletAddress: MutableState<String>,
    attrs: AttrsScope<HTMLDivElement>.() -> Unit = {}
) {
    InputGroup(
        attrs = attrs
    ) {
        AddOn("Wallet")
        Input(
            type = InputType.Text
        ) {
            classes("form-control")
            placeholder("Enter your wallet address")
            value(walletAddress.value)
            onInput { event -> walletAddress.value = event.value }
            maxLength(32)
        }
        AddOn(
            text = if (walletAddress.value.isBlank()) {
                "⚫"
            } else if (walletAddress.value.length != 32) {
                "🔴"
            } else {
                "🟢"
            },
            attrs = {
                if (walletAddress.value.isBlank()) {
                    title("No wallet added")
                } else if (walletAddress.value.length != 32) {
                    title("Invalid address")
                } else {
                    title("Valid address")
                }
            }
        )
    }
}