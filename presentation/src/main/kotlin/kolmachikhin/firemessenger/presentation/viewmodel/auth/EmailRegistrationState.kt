package kolmachikhin.firemessenger.presentation.viewmodel.auth

import kolmachikhin.alexander.validation.Validated
import kolmachikhin.firemessenger.auth.EmailRegistrationResult
import kolmachikhin.firemessenger.validation.EmailValidator
import kolmachikhin.firemessenger.validation.NicknameValidator
import kolmachikhin.firemessenger.validation.PasswordValidator

sealed class EmailRegistrationState {
    class Loading : EmailRegistrationState()

    sealed class Input : EmailRegistrationState() {
        abstract val nickname: String
        abstract val email: String
        abstract val password: String
        abstract val updateNickname: (String) -> Unit
        abstract val updateEmail: (String) -> Unit
        abstract val updatePassword: (String) -> Unit

        data class Correct(
            override val nickname: String,
            override val email: String,
            override val password: String,
            override val updateNickname: (String) -> Unit,
            override val updateEmail: (String) -> Unit,
            override val updatePassword: (String) -> Unit,
            val startRegistration: () -> Unit
        ) : Input()

        data class Incorrect(
            override val nickname: String,
            override val email: String,
            override val password: String,
            override val updateNickname: (String) -> Unit,
            override val updateEmail: (String) -> Unit,
            override val updatePassword: (String) -> Unit,
            val validatedNickname: Validated<String, NicknameValidator.IncorrectReason>?,
            val validatedEmail: Validated<String, EmailValidator.IncorrectReason>?,
            val validatedPassword: Validated<String, PasswordValidator.IncorrectReason>?
        ) : Input()
    }

    class RegistrationSuccess : EmailRegistrationState()

    class RegistrationFailed(
        val failedResult: EmailRegistrationResult.Failed,
        val retry: () -> Unit
    ) : EmailRegistrationState()
}