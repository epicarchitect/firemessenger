package kolmachikhin.fire.messenger.repository

import kolmachikhin.fire.messenger.data.User
import kolmachikhin.fire.messenger.validation.Correct
import kotlinx.coroutines.flow.StateFlow

abstract class UserRepository {

    abstract val state: StateFlow<State>

    abstract suspend fun updateNickname(nickname: Correct<String>)

    abstract suspend fun signOut()

    sealed class State {
        class NotAuthorized : State()
        class Loading : State()
        class Loaded(val user: User) : State()

        sealed class LoadingError : State() {
            class Disconnected : LoadingError()
            class ServiceUnavailable : LoadingError()
            class PermissionDenied : LoadingError()
            class Unknown : LoadingError()
        }
    }
}