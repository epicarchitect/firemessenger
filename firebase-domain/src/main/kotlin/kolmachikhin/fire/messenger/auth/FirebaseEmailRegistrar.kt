package kolmachikhin.fire.messenger.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.fire.messenger.data.User
import kolmachikhin.fire.messenger.mapper.toMap
import kolmachikhin.fire.messenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailRegistrar : EmailRegistrar() {

    override suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<Result> { continuation ->
        Firebase.auth.createUserWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Firebase.database.getReference("users")
                    .child(task.result.user!!.uid)
                    .setValue(
                        User(
                            id = task.result.user!!.uid,
                            nickname = nickname.data,
                            email = email.data
                        ).toMap()
                    ) { _, _ ->
                        continuation.resume(Result.Success())
                    }
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> Result.Failed.EmailAlreadyUsed(email.data)
                        is FirebaseNetworkException -> Result.Failed.ConnectionError()
                        else -> Result.Failed.Unknown()
                    }
                )
            }
        }
    }
}