package kolmachikhin.fire.messenger.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    userRepository: UserRepository
) : ViewModel() {

    val state = userRepository.userState.map {
        when (it) {
            is UserRepository.UserState.Authorizing -> {
                State("loading")
            }
            is UserRepository.UserState.Loaded -> {
                State("main")
            }
            is UserRepository.UserState.Loading -> {
                State("loading")
            }
            is UserRepository.UserState.NotAuthorized -> {
                State("email_registration")
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State("loading")
    )

    class State(
        val currentScreen: String
    )
}