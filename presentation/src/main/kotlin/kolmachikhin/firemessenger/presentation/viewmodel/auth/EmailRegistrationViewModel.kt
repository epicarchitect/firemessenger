package kolmachikhin.firemessenger.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.alexander.validation.Validated
import kolmachikhin.firemessenger.auth.EmailRegistrar
import kolmachikhin.firemessenger.auth.EmailRegistrationResult
import kolmachikhin.firemessenger.presentation.viewmodel.validateNullableWith
import kolmachikhin.firemessenger.validation.EmailValidator
import kolmachikhin.firemessenger.validation.NicknameValidator
import kolmachikhin.firemessenger.validation.PasswordValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmailRegistrationViewModel(
    private val registrar: EmailRegistrar,
    nicknameValidator: NicknameValidator,
    emailValidator: EmailValidator,
    passwordValidator: PasswordValidator
) : ViewModel() {

    private val nicknameState = MutableStateFlow<String?>(null)
    private val emailState = MutableStateFlow<String?>(null)
    private val passwordState = MutableStateFlow<String?>(null)
    private val registrationState = MutableStateFlow<RegistrationState?>(null)

    private val validatedNicknameState = nicknameState.validateNullableWith(nicknameValidator)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedEmailState = emailState.validateNullableWith(emailValidator)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val validatedPasswordState = passwordState.validateNullableWith(passwordValidator)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @Suppress("UNCHECKED_CAST")
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
                    is EmailRegistrationResult.Success -> {
                        EmailRegistrationState.RegistrationSuccess()
                    }
                    is EmailRegistrationResult.Failed -> {
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
        class Executed(val result: EmailRegistrationResult) : RegistrationState()
    }
}