package kolmachikhin.fire.messenger.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import kolmachikhin.fire.messenger.mapper.toUser
import kolmachikhin.fire.messenger.validation.Correct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUserRepository : UserRepository() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val gson = Gson()
    private val firebaseUserState = MutableStateFlow<FirebaseUser?>(null)
    private val userFirebaseReferenceState = MutableStateFlow<DatabaseReference?>(null)
    private val mutableUserState = MutableStateFlow<State>(State.Loading())
    private val userFirebaseReferenceListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val json = gson.fromJson(snapshot.value.toString(), JsonObject::class.java)
                mutableUserState.value = State.Loaded(json.toUser())
            } catch (t: Throwable) {
                Firebase.auth.signOut()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            mutableUserState.value = when (error.code) {
                DatabaseError.DISCONNECTED -> State.LoadingError.Disconnected()
                DatabaseError.UNAVAILABLE -> State.LoadingError.ServiceUnavailable()
                DatabaseError.PERMISSION_DENIED -> State.LoadingError.PermissionDenied()
                else -> State.LoadingError.Unknown()
            }
        }
    }

    override val state = mutableUserState

    init {
        Firebase.auth.addAuthStateListener {
            userFirebaseReferenceState.value?.removeEventListener(userFirebaseReferenceListener)
            firebaseUserState.value = it.currentUser

            if (it.currentUser == null) {
                mutableUserState.value = State.NotAuthorized()
            }
        }

        firebaseUserState.onEach {
            userFirebaseReferenceState.value = it?.let { user ->
                Firebase.database.getReference("users").child(user.uid)
            }
        }.launchIn(coroutineScope)

        userFirebaseReferenceState.onEach {
            it?.addValueEventListener(userFirebaseReferenceListener)
        }.launchIn(coroutineScope)
    }

    override suspend fun updateNickname(nickname: Correct<String>) = suspendCoroutine<Unit> { continuation ->
        Firebase.database.getReference("users")
            .child(Firebase.auth.currentUser!!.uid)
            .child("nickname")
            .setValue(nickname.data) { _, _ ->
                continuation.resume(Unit)
            }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }
}