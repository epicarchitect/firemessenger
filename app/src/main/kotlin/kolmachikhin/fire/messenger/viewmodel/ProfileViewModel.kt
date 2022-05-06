package kolmachikhin.fire.messenger.viewmodel

import androidx.lifecycle.ViewModel
import kolmachikhin.fire.messenger.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    val user = userRepository.userState

    fun signOut() {
        userRepository.signOut()
    }

}