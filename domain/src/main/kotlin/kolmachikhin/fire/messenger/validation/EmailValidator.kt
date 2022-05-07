package kolmachikhin.fire.messenger.validation

class EmailValidator {

    fun validate(value: String) = when {
        value.isEmpty() -> {
            Incorrect(value, IncorrectReason.Empty())
        }
        !value.matches(EMAIL_REGEX) -> {
            Incorrect(value, IncorrectReason.Pattern())
        }
        else -> Correct(value)
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class Pattern : IncorrectReason()
    }

    companion object {
        private val EMAIL_REGEX = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
        )
    }
}