@file:Suppress("UNCHECKED_CAST")

package kolmachikhin.fire.messenger.core.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.auth.EmailRegistrar
import kolmachikhin.fire.messenger.validation.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmailRegistrationViewModel(
    private val registrar: EmailRegistrar,
    private val nicknameValidator: NicknameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val nicknameState = MutableStateFlow<String?>(null)
    private val emailState = MutableStateFlow<String?>(null)
    private val passwordState = MutableStateFlow<String?>(null)
    private val registrationState = MutableStateFlow<RegistrationState?>(null)

    private val validatedNicknameState = nicknameState.map {
        it?.let(nicknameValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedEmailState = emailState.map {
        it?.let(emailValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedPasswordState = passwordState.map {
        it?.let(passwordValidator::validate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val state = combine(
        nicknameState,
        emailState,
        passwordState,
        validatedNicknameState,
        validatedEmailState,
        validatedPasswordState,
        registrationState
    ) {
        val nickname = it[0] as? String
        val email = it[1] as? String
        val password = it[2] as? String
        val validatedNickname = it[3] as? Validated<String, NicknameValidator.IncorrectReason>
        val validatedEmail = it[4] as? Validated<String, EmailValidator.IncorrectReason>
        val validatedPassword = it[5] as? Validated<String, PasswordValidator.IncorrectReason>

        when (val registration = it[6] as? RegistrationState) {
            is RegistrationState.Executing -> {
                EmailRegistrationState.Loading()
            }
            is RegistrationState.Executed -> {
                when (registration.result) {
                    is EmailRegistrar.Result.Success -> {
                        EmailRegistrationState.RegistrationSuccess()
                    }
                    is EmailRegistrar.Result.Failed -> {
                        EmailRegistrationState.RegistrationFailed(
                            registration.result,
                            retry = {
                                registrationState.value = null
                            }
                        )
                    }
                }
            }
            else -> if (validatedNickname is Correct && validatedEmail is Correct && validatedPassword is Correct) {
                EmailRegistrationState.Input.Correct(
                    nickname = validatedNickname.data,
                    email = validatedEmail.data,
                    password = validatedPassword.data,
                    updateNickname = { nicknameState.value = it },
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                    startRegistration = {
                        registrationState.value = RegistrationState.Executing()
                        viewModelScope.launch {
                            registrationState.value = RegistrationState.Executed(
                                registrar.register(
                                    validatedNickname,
                                    validatedEmail,
                                    validatedPassword
                                )
                            )
                        }
                    }
                )
            } else {
                EmailRegistrationState.Input.Incorrect(
                    nickname = nickname ?: "",
                    email = email ?: "",
                    password = password ?: "",
                    validatedNickname = validatedNickname,
                    validatedEmail = validatedEmail,
                    validatedPassword = validatedPassword,
                    updateNickname = { nicknameState.value = it },
                    updateEmail = { emailState.value = it },
                    updatePassword = { passwordState.value = it },
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        EmailRegistrationState.Loading()
    )

    private sealed class RegistrationState {
        class Executing : RegistrationState()
        class Executed(val result: EmailRegistrar.Result) : RegistrationState()
    }
}