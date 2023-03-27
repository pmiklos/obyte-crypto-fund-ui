package wallet.usecase

import wallet.domain.WalletValidation
import wallet.domain.WalletValidationResult

class ValidateWalletAddressUseCase(
    private var walletValidation: WalletValidation
) {

    operator fun invoke(address: String): WalletValidationResult = when {
        address.isBlank() -> WalletValidationResult.Empty
        else -> walletValidation.validateWalletAddress(address)
    }
}