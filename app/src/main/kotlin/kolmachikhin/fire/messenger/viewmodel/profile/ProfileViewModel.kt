package kolmachikhin.fire.messenger.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.validation.Correct
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(userRepository: UserRepository) : ViewModel() {

    val state = userRepository.state.map {
        when (it) {
            is UserRepository.State.Loaded -> ProfileState.Loaded(
                nickname = it.user.nickname,
                email = it.user.email,
                updateNickname = { nickname ->
                    viewModelScope.launch {
                        userRepository.updateNickname(Correct(nickname))
                    }
                },
                signOut = {
                    viewModelScope.launch {
                        userRepository.signOut()
                    }
                }
            )
            else -> ProfileState.Loading()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ProfileState.Loading())

}