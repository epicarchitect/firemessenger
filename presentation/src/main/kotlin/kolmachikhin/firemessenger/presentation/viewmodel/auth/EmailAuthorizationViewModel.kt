package kolmachikhin.firemessenger.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.auth.EmailAuthorizationResult
import kolmachikhin.firemessenger.auth.EmailAuthorizer
import kolmachikhin.firemessenger.validation.Correct
import kolmachikhin.firemessenger.validation.EmailValidator
import kolmachikhin.firemessenger.validation.PasswordValidator
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
                EmailAuthorizationState.Loading()
            }
            is AuthorizationState.Executed -> {
                when (authorization.result) {
                    is EmailAuthorizationResult.Success -> {
                        EmailAuthorizationState.AuthorizationSuccess()
                    }
                    is EmailAuthorizationResult.Failed -> {
                        EmailAuthorizationState.AuthorizationFailed(
                            authorization.result,
                            retry = {
                                authorizationState.value = null
                            }
                        )
                    }
                }
            }
            else -> if (validatedEmail is Correct && validatedPassword is Correct) {
                EmailAuthorizationState.Input.Correct(
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
                EmailAuthorizationState.Input.Incorrect(
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
        EmailAuthorizationState.Loading()
    )

    private sealed class AuthorizationState {
        class Executing : AuthorizationState()
        class Executed(val result: EmailAuthorizationResult) : AuthorizationState()
    }
}