package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UsersRepository {

    val state: StateFlow<UsersState>

    fun userState(userId: String): Flow<UserData>

}