package kolmachikhin.fire.messenger.validation

class NicknameValidator(
    private val maxNicknameLength: Int,
    private val minNicknameLength: Int
) {

    fun validate(value: String) = when {
        value.isEmpty() -> {
            Incorrect(value, IncorrectReason.Empty())
        }
        value.length < minNicknameLength -> {
            Incorrect(value, IncorrectReason.TooShort(minNicknameLength))
        }
        value.length > maxNicknameLength -> {
            Incorrect(value, IncorrectReason.TooLong(maxNicknameLength))
        }
        else -> Correct(value)
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class TooLong(val maxNameLength: Int) : IncorrectReason()
        class TooShort(val minNameLength: Int) : IncorrectReason()
    }
}