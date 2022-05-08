package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    val state = userRepository.userState.map {
        when (it) {
            is UserRepository.UserState.Loaded -> State.Loaded(
                firstName = it.user.firstName,
                lastName = it.user.lastName,
                email = it.user.email,
                updateFirstName = {
                    viewModelScope.launch {
                        userRepository.updateFirstName(it)
                    }
                },
                updateLastName = {
                    viewModelScope.launch {
                        userRepository.updateLastName(it)
                    }
                }
            )
            else -> State.Loading()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State.Loading()
    )

    fun signOut() {
        userRepository.signOut()
    }

    sealed class State {
        class Loading : State()
        class Loaded(
            val firstName: String,
            val lastName: String,
            val email: String,
            val updateFirstName: (String) -> Unit,
            val updateLastName: (String) -> Unit
        ) : State()
    }
}