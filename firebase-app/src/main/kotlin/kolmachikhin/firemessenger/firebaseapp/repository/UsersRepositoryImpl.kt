package kolmachikhin.firemessenger.firebaseapp.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.firemessenger.data.UserId
import kolmachikhin.firemessenger.firebaseapp.mapper.toUser
import kolmachikhin.firemessenger.repository.UsersRepository
import kolmachikhin.firemessenger.repository.UsersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class UsersRepositoryImpl : UsersRepository {

    private val mutableState = MutableStateFlow<UsersState>(UsersState.Loading())
    override val state = mutableState

    init {
        Firebase.database.getReference(USERS_REFERENCE_NAME).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mutableState.value = UsersState.Loaded(
                        snapshot.children.mapNotNull {
                            try {
                                it.toUser()
                            } catch (t: Throwable) {
                                null
                            }
                        }
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    mutableState.value = UsersState.Loading()
                }
            }
        )
    }

    override fun userState(userId: UserId) = state.filterIsInstance<UsersState.Loaded>().map {
        it.users.firstOrNull {
            it.id == userId
        }
    }.filterNotNull()
}