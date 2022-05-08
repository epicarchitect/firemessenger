package kolmachikhin.fire.messenger.repository

import kolmachikhin.fire.messenger.data.User

sealed class CurrentUserState {
    class NotAuthorized : CurrentUserState()
    class Loading : CurrentUserState()
    class Loaded(val user: User) : CurrentUserState()

    sealed class LoadingError : CurrentUserState() {
        class Disconnected : LoadingError()
        class ServiceUnavailable : LoadingError()
        class PermissionDenied : LoadingError()
        class Unknown : LoadingError()
    }
}