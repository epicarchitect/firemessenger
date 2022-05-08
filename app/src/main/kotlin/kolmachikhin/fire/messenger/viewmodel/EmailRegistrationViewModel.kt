@file:Suppress("UNCHECKED_CAST")

package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.authorization.EmailRegistrar
import kolmachikhin.fire.messenger.validation.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmailRegistrationViewModel(
    private val registrar: EmailRegistrar,
    private val firstNameValidator: FirstNameValidator,
    private val lastNameValidator: LastNameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val firstNameState = MutableStateFlow<String?>(null)
    private val lastNameState = MutableStateFlow<String?>(null)
    private val emailState = MutableStateFlow<String?>(null)
    private val passwordState = MutableStateFlow<String?>(null)
    private val registrationState = MutableStateFlow<RegistrationState?>(null)

    private val validatedFirstNameState = firstNameState.map {
        it?.let(firstNameValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedLastNameState = lastNameState.map {
        it?.let(lastNameValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedEmailState = emailState.map {
        it?.let(emailValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedPasswordState = passwordState.map {
        it?.let(passwordValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val state = combine(
        firstNameState,
        lastNameState,
        emailState,
        passwordState,
        validatedFirstNameState,
        validatedLastNameState,
        validatedEmailState,
        validatedPasswordState,
        registrationState
    ) {
        val firstName = it[0] as? String
        val lastName = it[1] as? String
        val email = it[2] as? String
        val password = it[3] as? String
        val validatedFirstName = it[4] as? Validated<String, FirstNameValidator.IncorrectReason>
        val validatedLastName = it[5] as? Validated<String, LastNameValidator.IncorrectReason>
        val validatedEmail = it[6] as? Validated<String, EmailValidator.IncorrectReason>
        val validatedPassword = it[7] as? Validated<String, PasswordValidator.IncorrectReason>

        when (val registration = it[8] as? RegistrationState) {
            is RegistrationState.Executing -> {
                State.Loading()
            }
            is RegistrationState.Executed -> {
                when (registration.result) {
                    is EmailRegistrar.Result.Success -> {
                        State.RegistrationSuccess()
                    }
                    is EmailRegistrar.Result.Failed -> {
                        State.RegistrationFailed(
                            registration.result,
                            retry = {
                                registrationState.value = null
                            }
                        )
                    }
                }
            }
            else -> if (
                validatedFirstName is Correct &&
                validatedLastName is Correct &&
                validatedEmail is Correct &&
                validatedPassword is Correct
            ) {
                State.Input.Correct(
                    firstName = validatedFirstName.data,
                    lastName = validatedLastName.data,
                    email = validatedEmail.data,
                    password = validatedPassword.data,
                    updateFirstName = { firstNameState.value = it },
                    updateLastName = { lastNameState.value = it },
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                    startRegistration = {
                        registrationState.value = RegistrationState.Executing()
                        viewModelScope.launch {
                            registrationState.value = RegistrationState.Executed(
                                registrar.register(
                                    validatedFirstName,
                                    validatedLastName,
                                    validatedEmail,
                                    validatedPassword
                                )
                            )
                        }
                    }
                )
            } else {
                State.Input.Incorrect(
                    firstName = firstName ?: "",
                    lastName = lastName ?: "",
                    email = email ?: "",
                    password = password ?: "",
                    validatedFirstName = validatedFirstName,
                    validatedLastName = validatedLastName,
                    validatedEmail = validatedEmail,
                    validatedPassword = validatedPassword,
                    updateFirstName = { firstNameState.value = it },
                    updateLastName = { lastNameState.value = it },
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
            abstract val firstName: String
            abstract val lastName: String
            abstract val email: String
            abstract val password: String
            abstract val updateFirstName: (String) -> Unit
            abstract val updateLastName: (String) -> Unit
            abstract val updateEmail: (String) -> Unit
            abstract val updatePassword: (String) -> Unit

            data class Correct(
                override val firstName: String,
                override val lastName: String,
                override val email: String,
                override val password: String,
                override val updateFirstName: (String) -> Unit,
                override val updateLastName: (String) -> Unit,
                override val updateEmail: (String) -> Unit,
                override val updatePassword: (String) -> Unit,
                val startRegistration: () -> Unit
            ) : Input()

            data class Incorrect(
                override val firstName: String,
                override val lastName: String,
                override val email: String,
                override val password: String,
                override val updateFirstName: (String) -> Unit,
                override val updateLastName: (String) -> Unit,
                override val updateEmail: (String) -> Unit,
                override val updatePassword: (String) -> Unit,
                val validatedFirstName: Validated<String, FirstNameValidator.IncorrectReason>?,
                val validatedLastName: Validated<String, LastNameValidator.IncorrectReason>?,
                val validatedEmail: Validated<String, EmailValidator.IncorrectReason>?,
                val validatedPassword: Validated<String, PasswordValidator.IncorrectReason>?
            ) : Input()
        }

        class RegistrationSuccess : State()

        class RegistrationFailed(
            val result: EmailRegistrar.Result.Failed,
            val retry: () -> Unit
        ) : State()
    }

    private sealed class RegistrationState {
        class Executing : RegistrationState()
        class Executed(val result: EmailRegistrar.Result) : RegistrationState()
    }
}