@file:OptIn(FlowPreview::class)

package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.registration.EmailRegistrar
import kolmachikhin.fire.messenger.validation.Correct
import kolmachikhin.fire.messenger.validation.EmailValidator
import kolmachikhin.fire.messenger.validation.PasswordValidator
import kolmachikhin.fire.messenger.validation.ValidationResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmailRegistrationViewModel(
    private val registrar: EmailRegistrar,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val emailState = MutableStateFlow<String?>(null)
    private val passwordState = MutableStateFlow<String?>(null)
    private val registrationState = MutableStateFlow<RegistrationState?>(null)

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
        registrationState
    ) { email, password, validatedEmail, validatedPassword, registration ->
        when (registration) {
            is RegistrationState.InProcess -> {
                State.Loading()
            }
            is RegistrationState.Failed -> {
                State.RegistrationFailed(
                    retry = {
                        registrationState.value = null
                    }
                )
            }
            is RegistrationState.Success -> {
                State.RegistrationSuccess()
            }
            else -> if (validatedEmail is Correct && validatedPassword is Correct) {
                State.Input.Correct(
                    email = validatedEmail.data,
                    password = validatedPassword.data,
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                    startRegistration = {
                        registrationState.value = RegistrationState.InProcess()
                        viewModelScope.launch {
                            when (registrar.register(validatedEmail, validatedPassword)) {
                                is EmailRegistrar.Result.Success -> {
                                    registrationState.value = RegistrationState.Success()
                                }
                                is EmailRegistrar.Result.Failed -> {
                                    registrationState.value = RegistrationState.Failed()
                                }
                            }
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
                val startRegistration: () -> Unit
            ) : Input()

            data class Incorrect(
                override val email: String,
                override val password: String,
                override val updateEmail: (String) -> Unit,
                override val updatePassword: (String) -> Unit,
                val validatedEmail: ValidationResult<String, EmailValidator.IncorrectReason>?,
                val validatedPassword: ValidationResult<String, PasswordValidator.IncorrectReason>?
            ) : Input()
        }

        class RegistrationSuccess : State()

        class RegistrationFailed(
            val retry: () -> Unit
        ) : State()
    }

    sealed class RegistrationState {
        class InProcess : RegistrationState()
        class Success : RegistrationState()
        class Failed : RegistrationState()
    }
}