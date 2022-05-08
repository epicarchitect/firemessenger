package kolmachikhin.fire.messenger.viewmodel.app

import kolmachikhin.fire.messenger.repository.UserRepository

data class AppState(
    val userState: UserRepository.State
)