package kolmachikhin.firemessenger.validation

import kolmachikhin.alexander.validation.Validator

class NicknameValidator(
    private val maxNicknameLength: Int,
    private val minNicknameLength: Int
) : Validator<String, NicknameValidator.IncorrectReason>() {

    override suspend fun String.incorrectReason() = when {
        isEmpty() -> IncorrectReason.Empty()
        length < minNicknameLength -> IncorrectReason.TooShort(minNicknameLength)
        length > maxNicknameLength -> IncorrectReason.TooLong(maxNicknameLength)
        else -> null
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class TooLong(val maxNameLength: Int) : IncorrectReason()
        class TooShort(val minNameLength: Int) : IncorrectReason()
    }
}