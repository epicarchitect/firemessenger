package kolmachikhin.fire.messenger.core.viewmodel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.CurrentUserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(currentUserRepository: CurrentUserRepository) : ViewModel() {

    val state = currentUserRepository.state.map {
        AppState(currentUserState = it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AppState(currentUserRepository.state.value)
    )

}