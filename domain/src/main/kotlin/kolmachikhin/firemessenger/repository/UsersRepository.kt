package kolmachikhin.firemessenger.repository

import kotlinx.coroutines.flow.StateFlow

interface UsersRepository {

    val state: StateFlow<UsersState>

}