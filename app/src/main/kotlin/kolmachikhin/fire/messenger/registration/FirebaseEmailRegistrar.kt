package kolmachikhin.fire.messenger.registration

import com.google.firebase.auth.FirebaseAuth
import kolmachikhin.fire.messenger.registration.EmailRegistrar
import kolmachikhin.fire.messenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailRegistrar(
    private val firebaseAuth: FirebaseAuth
) : EmailRegistrar {

    override suspend fun register(
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<EmailRegistrar.Result> { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(EmailRegistrar.Result.Success())
            } else {
                task.exception?.printStackTrace()
                continuation.resume(EmailRegistrar.Result.Failed())
            }
        }
    }
}