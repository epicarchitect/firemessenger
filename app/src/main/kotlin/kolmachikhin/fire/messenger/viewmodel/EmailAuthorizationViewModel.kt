package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.authorization.EmailAuthorizer
import kolmachikhin.fire.messenger.validation.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmailAuthorizationViewModel(
    private val authorizer: EmailAuthorizer,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val emailState = MutableStateFlow<String?>(null)
    private val passwordState = MutableStateFlow<String?>(null)
    private val authorizationState = MutableStateFlow<AuthorizationState?>(null)

    private val validatedEmailState = emailState.map {
        it?.let(emailValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedPasswordState = passwordState.map {
        it?.let(passwordValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val state = combine(
        emailState,
        passwordState,
        validatedEmailState,
        validatedPasswordState,
        authorizationState
    ) { email, password, validatedEmail, validatedPassword, authorization ->
        when (authorization) {
            is AuthorizationState.Executing -> {
                State.Loading()
            }
            is AuthorizationState.Executed -> {
                when (authorization.result) {
                    is EmailAuthorizer.Result.Success -> {
                        State.AuthorizationSuccess()
                    }
                    is EmailAuthorizer.Result.Failed -> {
                        State.AuthorizationFailed(
                            authorization.result,
                            retry = {
                                authorizationState.value = null
                            }
                        )
                    }
                }
            }
            else -> if (validatedEmail is Correct && validatedPassword is Correct) {
                State.Input.Correct(
                    email = validatedEmail.data,
                    password = validatedPassword.data,
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                    startAuthorization = {
                        authorizationState.value = AuthorizationState.Executing()
                        viewModelScope.launch {
                            authorizationState.value = AuthorizationState.Executed(
                                authorizer.authorize(validatedEmail, validatedPassword)
                            )
                        }
                    }
                )
            } else {
                State.Input.Incorrect(
                    email = email ?: "",
                    password = password ?: "",
                    validatedEmail = validatedEmail,
                    validatedPassword = validatedPassword,
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State.Loading()
    )

    sealed class State {
        class Loading : State()

        sealed class Input : State() {
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

        class AuthorizationSuccess : State()

        class AuthorizationFailed(
            val result: EmailAuthorizer.Result.Failed,
            val retry: () -> Unit
        ) : State()
    }

    private sealed class AuthorizationState {
        class Executing : AuthorizationState()
        class Executed(val result: EmailAuthorizer.Result) : AuthorizationState()
    }
}