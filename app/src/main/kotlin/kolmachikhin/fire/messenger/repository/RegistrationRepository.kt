package kolmachikhin.fire.messenger.repository

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegistrationRepository(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun register(email: String, password: String) = suspendCoroutine<RegistrationResult> { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(RegistrationResult.Success())
            } else {
                task.exception?.printStackTrace()
                continuation.resume(RegistrationResult.Failed())
            }
        }
    }

    sealed class RegistrationResult {
        class Success : RegistrationResult()
        class Failed : RegistrationResult()
    }
}