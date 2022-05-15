package kolmachikhin.firemessenger.firebaseapp.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.firemessenger.auth.EmailRegistrar
import kolmachikhin.firemessenger.auth.EmailRegistrationResult
import kolmachikhin.firemessenger.data.UserData
import kolmachikhin.firemessenger.firebaseapp.mapper.toMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailRegistrar : EmailRegistrar {

    override suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ) = suspendCoroutine<EmailRegistrationResult> { continuation ->
        Firebase.auth.createUserWithEmailAndPassword(
            email.data,
            password.data
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Firebase.database.getReference("users")
                    .child(task.result.user!!.uid)
                    .setValue(
                        UserData(
                            id = task.result.user!!.uid,
                            nickname = nickname.data,
                            email = email.data
                        ).toMap()
                    ) { _, _ ->
                        continuation.resume(EmailRegistrationResult.Success())
                    }
            } else {
                task.exception?.printStackTrace()
                continuation.resume(
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> EmailRegistrationResult.Failed.EmailAlreadyUsed(email.data)
                        is FirebaseNetworkException -> EmailRegistrationResult.Failed.ConnectionError()
                        else -> EmailRegistrationResult.Failed.Unknown()
                    }
                )
            }
        }
    }
}