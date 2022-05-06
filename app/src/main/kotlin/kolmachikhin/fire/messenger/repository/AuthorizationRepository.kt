package kolmachikhin.fire.messenger.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthorizationRepository(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun register(email: String, password: String) = suspendCancellableCoroutine<Unit> { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(checkNotNull(task.exception))
            }
        }
    }
}