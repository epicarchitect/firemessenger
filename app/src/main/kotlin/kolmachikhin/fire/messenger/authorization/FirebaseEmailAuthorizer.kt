package kolmachikhin.fire.messenger.authorization

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kolmachikhin.fire.messenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailAuthorizer(
    private val firebaseAuth: FirebaseAuth
) : EmailAuthorizer {

    override suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<EmailAuthorizer.Result> { continuation ->
        firebaseAuth.signInWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(EmailAuthorizer.Result.Success())
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseNetworkException -> EmailAuthorizer.Result.Failed.ConnectionError()
                        else -> EmailAuthorizer.Result.Failed.Unknown()
                    }
                )
            }
        }
    }
}