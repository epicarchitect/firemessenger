package kolmachikhin.firemessenger.presentation.viewmodel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.MyUserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(myUserRepository: MyUserRepository) : ViewModel() {

    val state = myUserRepository.state.map {
        AppState(myUserState = it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AppState(myUserRepository.state.value)
    )

}