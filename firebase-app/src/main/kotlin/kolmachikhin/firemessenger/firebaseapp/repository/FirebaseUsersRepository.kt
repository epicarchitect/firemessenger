package kolmachikhin.firemessenger.firebaseapp.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.firemessenger.firebaseapp.mapper.toUser
import kolmachikhin.firemessenger.repository.UsersRepository
import kolmachikhin.firemessenger.repository.UsersState
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