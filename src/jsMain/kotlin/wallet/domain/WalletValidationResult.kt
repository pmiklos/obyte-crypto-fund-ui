package wallet.domain

sealed class WalletValidationResult(val description: String) {
    object Empty: WalletValidationResult("No address specified")
    object Valid: WalletValidationResult("Valid wallet address")
    data class Invalid(val error: String): WalletValidationResult(error)
}