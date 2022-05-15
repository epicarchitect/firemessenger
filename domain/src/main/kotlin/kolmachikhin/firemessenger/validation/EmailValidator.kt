package kolmachikhin.firemessenger.validation

import kolmachikhin.alexander.validation.Validator

class EmailValidator : Validator<String, EmailValidator.IncorrectReason>() {

    override suspend fun String.incorrectReason() = when {
        isEmpty() -> IncorrectReason.Empty()
        !matches(EMAIL_REGEX) -> IncorrectReason.Pattern()
        else -> null
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class Pattern : IncorrectReason()
    }

    companion object {
        private val EMAIL_REGEX =  "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+".toRegex()
    }
}
