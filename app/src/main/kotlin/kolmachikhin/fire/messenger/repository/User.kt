package kolmachikhin.fire.messenger.repository

import android.util.Patterns

class User(
    val firstMame: String,
    val lastName: String,
    val birthdate: Long,
    val email: String
) {
    init {
        Patterns.EMAIL_ADDRESS
    }
}