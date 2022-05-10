package kolmachikhin.firemessenger.validation

class PasswordValidator(
    private val minPasswordLength: Int
) {

    fun validate(value: String) = when {
        value.isEmpty() -> {
            Incorrect(value, IncorrectReason.Empty())
        }
        value.length < minPasswordLength -> {
            Incorrect(value, IncorrectReason.Short(minPasswordLength))
        }
        else -> Correct(value)
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class Short(val minPasswordLength: Int) : IncorrectReason()
    }
}