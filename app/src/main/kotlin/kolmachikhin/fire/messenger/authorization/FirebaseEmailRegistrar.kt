package kolmachikhin.fire.messenger.authorization

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kolmachikhin.fire.messenger.repository.User
import kolmachikhin.fire.messenger.repository.toMap
import kolmachikhin.fire.messenger.validation.Correct
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailRegistrar(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : EmailRegistrar {

    override suspend fun register(
        firstName: Correct<String>,
        lastName: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<EmailRegistrar.Result> { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseDatabase.getReference("users").child(task.result.user!!.uid).setValue(
                    User(
                        id = task.result.user!!.uid,
                        firstName = firstName.data,
                        lastName = lastName.data,
                        email = email.data
                    ).toMap()
                ) { _, _ ->
                    continuation.resume(EmailRegistrar.Result.Success())
                }
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> EmailRegistrar.Result.Failed.EmailAlreadyUsed(email.data)
                        is FirebaseNetworkException -> EmailRegistrar.Result.Failed.ConnectionError()
                        else -> EmailRegistrar.Result.Failed.Unknown()
                    }
                )
            }
        }
    }
}