package kolmachikhin.fire.messenger.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kolmachikhin.fire.messenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailAuthorizer : EmailAuthorizer() {

    override suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<Result> { continuation ->
        Firebase.auth.signInWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Result.Success())
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseNetworkException -> Result.Failed.ConnectionError()
                        else -> Result.Failed.Unknown()
                    }
                )
            }
        }
    }
}