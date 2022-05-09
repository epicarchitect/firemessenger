package kolmachikhin.fire.messenger.core.viewmodel.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.CurrentUserRepository
import kolmachikhin.fire.messenger.repository.CurrentUserState
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