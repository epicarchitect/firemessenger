package kolmachikhin.fire.messenger.validation

import android.util.Patterns

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
        private val EMAIL_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
    }
}