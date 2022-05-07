package kolmachikhin.fire.messenger.validation

class PasswordValidator(
    val minPasswordLength: Int
) {

    fun validate(value: String) = when {
        value.isEmpty() -> {
            Incorrect(value, IncorrectReason.Empty())
        }
        value.length < minPasswordLength -> {
            Incorrect(value, IncorrectReason.Short())
        }
        else -> Correct(value)
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class Short : IncorrectReason()
    }
}