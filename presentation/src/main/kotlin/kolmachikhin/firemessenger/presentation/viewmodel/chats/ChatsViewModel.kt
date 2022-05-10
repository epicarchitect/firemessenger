package kolmachikhin.firemessenger.presentation.viewmodel.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.CurrentUserRepository
import kolmachikhin.firemessenger.repository.CurrentUserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ChatsViewModel(
    currentUserRepository: CurrentUserRepository
) : ViewModel() {

    val state = currentUserRepository.state.map {
        when (it) {
            is CurrentUserState.Loaded -> ChatsState.Loaded(it.user.nickname)
            else -> ChatsState.Loading()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ChatsState.Loading()
    )

}