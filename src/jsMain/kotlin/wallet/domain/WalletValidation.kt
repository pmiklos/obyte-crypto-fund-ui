package wallet.domain

interface WalletValidation {
    fun validateWalletAddress(address: String): WalletValidationResult
}