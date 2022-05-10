package kolmachikhin.firemessenger.presentation.viewmodel.auth

import kolmachikhin.firemessenger.auth.EmailAuthorizationResult
import kolmachikhin.firemessenger.validation.EmailValidator
import kolmachikhin.firemessenger.validation.PasswordValidator
import kolmachikhin.firemessenger.validation.Validated

sealed class EmailAuthorizationState {
    class Loading : EmailAuthorizationState()

    sealed class Input : EmailAuthorizationState() {
        abstract val email: String
        abstract val password: String
        abstract val updateEmail: (String) -> Unit
        abstract val updatePassword: (String) -> Unit

        data class Correct(
            override val email: String,
            override val password: String,
            override val updateEmail: (String) -> Unit,
            override val updatePassword: (String) -> Unit,
            val startAuthorization: () -> Unit
        ) : Input()

        data class Incorrect(
            override val email: String,
            override val password: String,
            override val updateEmail: (String) -> Unit,
            override val updatePassword: (String) -> Unit,
            val validatedEmail: Validated<String, EmailValidator.IncorrectReason>?,
            val validatedPassword: Validated<String, PasswordValidator.IncorrectReason>?
        ) : Input()
    }

    class AuthorizationSuccess : EmailAuthorizationState()

    class AuthorizationFailed(
        val failedResult: EmailAuthorizationResult.Failed,
        val retry: () -> Unit
    ) : EmailAuthorizationState()
}