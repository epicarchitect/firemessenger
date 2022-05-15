package kolmachikhin.firemessenger.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.data.UserId
import kolmachikhin.firemessenger.repository.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    usersRepository: UsersRepository,
    userId: UserId
) : ViewModel() {

    val state = usersRepository.userState(userId).map {
        ProfileState.Loaded(
            nickname = it.nickname,
            email = it.email
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ProfileState.Loading())

}