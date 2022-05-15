package kolmachikhin.firemessenger.presentation.viewmodel.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.MyUserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ChatsViewModel(
    myUserRepository: MyUserRepository
) : ViewModel() {

    val state = myUserRepository.state.map {
        when (it) {
            is MyUserState.Loaded -> ChatsState.Loaded(it.user.nickname)
            else -> ChatsState.Loading()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ChatsState.Loading()
    )

}