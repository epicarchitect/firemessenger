package kolmachikhin.fire.messenger.repository

import kolmachikhin.fire.messenger.data.User

sealed class UsersState {
    class Loading : UsersState()
    class Loaded(val users: List<User>) : UsersState()
}