package kolmachikhin.fire.messenger.core.viewmodel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.fire.messenger.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(userRepository: UserRepository) : ViewModel() {

    val state = userRepository.state.map {
        AppState(userState = it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AppState(userRepository.state.value)
    )

}