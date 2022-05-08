package kolmachikhin.fire.messenger.validation

class FirstNameValidator(
    private val maxNameLength: Int
) {

    fun validate(value: String) = when {
        value.isEmpty() -> {
            Incorrect(value, IncorrectReason.Empty())
        }
        value.length > maxNameLength -> {
            Incorrect(value, IncorrectReason.TooLong(maxNameLength))
        }
        else -> Correct(value)
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class TooLong(val maxNameLength: Int) : IncorrectReason()
    }
}