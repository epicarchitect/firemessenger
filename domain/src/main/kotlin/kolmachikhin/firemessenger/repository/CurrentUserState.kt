package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.UserData

sealed class CurrentUserState {
    class NotAuthorized : CurrentUserState()
    class Loading : CurrentUserState()
    class Loaded(val user: UserData) : CurrentUserState()

    sealed class LoadingError : CurrentUserState() {
        class Disconnected : LoadingError()
        class ServiceUnavailable : LoadingError()
        class PermissionDenied : LoadingError()
        class Unknown : LoadingError()
    }
}