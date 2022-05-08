package kolmachikhin.fire.messenger.core.viewmodel.app

import kolmachikhin.fire.messenger.repository.UserRepository

data class AppState(
    val userState: UserRepository.State
)