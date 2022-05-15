package kolmachikhin.firemessenger.firebaseapp.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.firemessenger.firebaseapp.mapper.toUser
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.MyUserState
import kotlinx.coroutines.flow.MutableStateFlow

class MyUserRepositoryImpl : MyUserRepository {

    private var userReference: DatabaseReference? = null
    private val mutableState = MutableStateFlow<MyUserState>(MyUserState.Loading())
    override val state = mutableState

    private val userReferenceListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                mutableState.value = MyUserState.Loaded(snapshot.toUser())
            } catch (t: Throwable) {
                Firebase.auth.signOut()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            mutableState.value = when (error.code) {
                DatabaseError.DISCONNECTED -> MyUserState.LoadingError.Disconnected()
                DatabaseError.UNAVAILABLE -> MyUserState.LoadingError.ServiceUnavailable()
                DatabaseError.PERMISSION_DENIED -> MyUserState.LoadingError.PermissionDenied()
                else -> MyUserState.LoadingError.Unknown()
            }
        }
    }

    init {
        Firebase.auth.addAuthStateListener {
            userReference?.removeEventListener(userReferenceListener)
            userReference = null

            when (val firebaseUser = it.currentUser) {
                null -> {
                    mutableState.value = MyUserState.NotAuthorized()
                }
                else -> {
                    userReference = Firebase.database
                        .getReference(USERS_REFERENCE_NAME)
                        .child(firebaseUser.uid)
                        .also {
                            it.addValueEventListener(userReferenceListener)
                        }
                }
            }
        }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }
}