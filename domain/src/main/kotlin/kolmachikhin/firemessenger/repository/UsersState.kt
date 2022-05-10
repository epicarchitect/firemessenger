package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.UserData

sealed class UsersState {
    class Loading : UsersState()
    class Loaded(val users: List<UserData>) : UsersState()
}