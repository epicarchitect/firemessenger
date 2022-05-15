package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.UserData

sealed class MyUserState {
    class NotAuthorized : MyUserState()
    class Loading : MyUserState()
    class Loaded(val user: UserData) : MyUserState()

    sealed class LoadingError : MyUserState() {
        class Disconnected : LoadingError()
        class ServiceUnavailable : LoadingError()
        class PermissionDenied : LoadingError()
        class Unknown : LoadingError()
    }
}