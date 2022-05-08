package kolmachikhin.fire.messenger.core.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.CurrentUserRepository
import kolmachikhin.fire.messenger.repository.CurrentUserState
import kolmachikhin.fire.messenger.validation.Correct
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(currentUserRepository: CurrentUserRepository) : ViewModel() {

    val state = currentUserRepository.state.map {
        when (it) {
            is CurrentUserState.Loaded -> ProfileState.Loaded(
                nickname = it.user.nickname,
                email = it.user.email,
                updateNickname = { nickname ->
                    viewModelScope.launch {
                        currentUserRepository.updateNickname(Correct(nickname))
                    }
                },
                signOut = {
                    viewModelScope.launch {
                        currentUserRepository.signOut()
                    }
                }
            )
            else -> ProfileState.Loading()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ProfileState.Loading())

}