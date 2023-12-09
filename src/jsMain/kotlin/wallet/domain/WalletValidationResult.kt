package wallet.domain

sealed class WalletValidationResult(val description: String) {
    data object Empty: WalletValidationResult("No address specified")
    data object Valid: WalletValidationResult("Valid wallet address")
    data class Invalid(val error: String): WalletValidationResult(error)
}