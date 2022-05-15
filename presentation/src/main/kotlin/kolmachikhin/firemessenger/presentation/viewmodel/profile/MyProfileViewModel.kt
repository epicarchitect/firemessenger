package kolmachikhin.firemessenger.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.MyUserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyProfileViewModel(
    myUserRepository: MyUserRepository
) : ViewModel() {

    val state = myUserRepository.state.map {
        when (it) {
            is MyUserState.Loaded -> MyProfileState.Loaded(
                nickname = it.user.nickname,
                email = it.user.email,
                signOut = {
                    viewModelScope.launch {
                        myUserRepository.signOut()
                    }
                }
            )
            else -> MyProfileState.Loading()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MyProfileState.Loading())

}