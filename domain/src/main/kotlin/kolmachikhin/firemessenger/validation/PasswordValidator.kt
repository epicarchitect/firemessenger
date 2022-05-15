package kolmachikhin.firemessenger.validation

import kolmachikhin.alexander.validation.Validator

class PasswordValidator(
    private val minPasswordLength: Int
) : Validator<String, PasswordValidator.IncorrectReason>() {

    override suspend fun String.incorrectReason() = when {
        isEmpty() -> IncorrectReason.Empty()
        length < minPasswordLength -> IncorrectReason.Short(minPasswordLength)
        else -> null
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class Short(val minPasswordLength: Int) : IncorrectReason()
    }
}