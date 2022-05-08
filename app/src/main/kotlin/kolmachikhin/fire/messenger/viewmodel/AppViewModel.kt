package kolmachikhin.fire.messenger.viewmodel

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
        State(userState = it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State(userRepository.userState.value)
    )

    class State(
        val userState: UserRepository.UserState
    )
}