package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.compose.screen.Registration
import kolmachikhin.fire.messenger.repository.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow<State>(
        State.Input(
            email = "",
            password = ""
        )
    )

    val state = mutableState.asStateFlow()

    fun setEmail(email: String) {
        mutableState.update {
            (it as State.Input).copy(
                email = email
            )
        }
    }

    fun setPassword(password: String) {
        mutableState.update {
            (it as State.Input).copy(
                password = password
            )
        }
    }

    fun register() = viewModelScope.launch {
        val state = mutableState.getAndUpdate {
            State.Registration()
        } as State.Input

        try {
            val result = registrationRepository.register(
                email = state.email,
                password = state.password
            )

            when (result) {
                is RegistrationRepository.RegistrationResult.Success -> {
                    mutableState.value = State.Registered()
                }
                is RegistrationRepository.RegistrationResult.Failed -> {
                    mutableState.value = State.RegistrationFailed()
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            mutableState.value = State.RegistrationFailed()
        }
    }

    sealed class State {
        data class Input(
            val email: String,
            val password: String
        ) : State()

        class Registration : State()
        class Registered : State()
        class RegistrationFailed : State()
    }
}