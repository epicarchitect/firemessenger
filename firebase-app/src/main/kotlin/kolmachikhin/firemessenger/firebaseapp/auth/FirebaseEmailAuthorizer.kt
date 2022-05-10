package kolmachikhin.firemessenger.firebaseapp.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kolmachikhin.firemessenger.auth.EmailAuthorizationResult
import kolmachikhin.firemessenger.auth.EmailAuthorizer
import kolmachikhin.firemessenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailAuthorizer : EmailAuthorizer {

    override suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<EmailAuthorizationResult> { continuation ->
        Firebase.auth.signInWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(EmailAuthorizationResult.Success())
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseNetworkException -> EmailAuthorizationResult.Failed.ConnectionError()
                        else -> EmailAuthorizationResult.Failed.Unknown()
                    }
                )
            }
        }
    }
}