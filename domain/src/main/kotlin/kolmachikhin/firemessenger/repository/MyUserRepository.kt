package kolmachikhin.firemessenger.repository

import kotlinx.coroutines.flow.StateFlow

interface MyUserRepository {

    val state: StateFlow<MyUserState>

    suspend fun signOut()

}