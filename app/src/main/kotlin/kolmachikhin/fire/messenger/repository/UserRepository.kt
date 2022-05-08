package kolmachikhin.fire.messenger.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepository(
    coroutineScope: CoroutineScope,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val gson: Gson
) {
    private val firebaseUserState = MutableStateFlow<FirebaseUser?>(null)
    private val userFirebaseReferenceState = MutableStateFlow<DatabaseReference?>(null)
    private val mutableUserState = MutableStateFlow<UserState>(UserState.Loading())

    private val userFirebaseReferenceListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val json = gson.fromJson(snapshot.value.toString(), JsonObject::class.java)
                mutableUserState.value = UserState.Loaded(json.toUser())
            } catch (t: Throwable) {
                signOut()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            mutableUserState.value = when (error.code) {
                DatabaseError.DISCONNECTED -> UserState.LoadingError.Disconnected()
                DatabaseError.UNAVAILABLE -> UserState.LoadingError.ServiceUnavailable()
                DatabaseError.PERMISSION_DENIED -> UserState.LoadingError.PermissionDenied()
                else -> UserState.LoadingError.Unknown()
            }
        }
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

    suspend fun updateLastName(lastName: String) = suspendCoroutine<Unit> { continuation ->
        firebaseDatabase.getReference("users")
            .child(firebaseAuth.currentUser!!.uid)
            .child("lastName")
            .setValue(lastName) { _, _ ->
                continuation.resume(Unit)
            }
    }

    suspend fun updateFirstName(firstName: String) = suspendCoroutine<Unit> { continuation ->
        firebaseDatabase.getReference("users")
            .child(firebaseAuth.currentUser!!.uid)
            .child("firstName")
            .setValue(firstName) { _, _ ->
                continuation.resume(Unit)
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    sealed class UserState {
        class NotAuthorized : UserState()
        class Loading : UserState()
        class Loaded(val user: User) : UserState()
        sealed class LoadingError : UserState() {
            class Disconnected : LoadingError()
            class ServiceUnavailable : LoadingError()
            class PermissionDenied : LoadingError()
            class Unknown : LoadingError()
        }
    }
}