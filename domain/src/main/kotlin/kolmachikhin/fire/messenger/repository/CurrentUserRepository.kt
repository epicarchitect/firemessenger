package kolmachikhin.fire.messenger.repository

import kolmachikhin.fire.messenger.validation.Correct
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserRepository {

    val state: StateFlow<CurrentUserState>

    suspend fun updateNickname(nickname: Correct<String>)

    suspend fun signOut()

}