package kolmachikhin.fire.messenger.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserRepository(
    coroutineScope: CoroutineScope,
    private val firebaseAuth: FirebaseAuth,
    firebaseDatabase: FirebaseDatabase,
    private val gson: Gson
) {
    private val userFirebaseReferenceState = MutableStateFlow<DatabaseReference?>(null)
    private val firebaseUserState = MutableStateFlow(firebaseAuth.currentUser)
    private val mutableUserState = MutableStateFlow(
        if (firebaseAuth.currentUser == null) {
            UserState.NotAuthorized()
        } else {
            UserState.Loading()
        }
    )

    private val userFirebaseReferenceListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val json = gson.fromJson(snapshot.value.toString(), JsonObject::class.java)
            mutableUserState.value = UserState.Loaded(json.toUser())
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    val userState = mutableUserState.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener {
            userFirebaseReferenceState.value?.removeEventListener(userFirebaseReferenceListener)
            firebaseUserState.value = it.currentUser

            if (it.currentUser == null) {
                mutableUserState.value = UserState.NotAuthorized()
            }
        }

        firebaseUserState.onEach {
            userFirebaseReferenceState.value = it?.let { user ->
                firebaseDatabase.getReference("users").child(user.uid)
            }
        }.launchIn(coroutineScope)

        userFirebaseReferenceState.onEach {
            it?.addValueEventListener(userFirebaseReferenceListener)
        }.launchIn(coroutineScope)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    sealed class UserState {
        class NotAuthorized : UserState()
        class Authorizing : UserState()
        class Loading : UserState()
        class Loaded(val user: User) : UserState()
    }
}