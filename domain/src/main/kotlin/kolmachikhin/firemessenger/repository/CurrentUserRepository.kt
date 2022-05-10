package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.validation.Correct
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserRepository {

    val state: StateFlow<CurrentUserState>

    suspend fun updateNickname(nickname: Correct<String>)

    suspend fun signOut()

}