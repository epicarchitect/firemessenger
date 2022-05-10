package kolmachikhin.firemessenger.presentation.viewmodel.app

import kolmachikhin.firemessenger.repository.CurrentUserState

data class AppState(
    val currentUserState: CurrentUserState
)