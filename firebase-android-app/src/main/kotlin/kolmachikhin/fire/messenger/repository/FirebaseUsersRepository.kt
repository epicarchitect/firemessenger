package kolmachikhin.fire.messenger.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.fire.messenger.mapper.toUser
import kotlinx.coroutines.flow.MutableStateFlow

class FirebaseUsersRepository : UsersRepository {

    private val mutableState = MutableStateFlow<UsersState>(UsersState.Loading())
    override val state = mutableState

    init {
        Firebase.database.getReference("users").addValueEventListener(
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

                }
            }
        )
    }
}