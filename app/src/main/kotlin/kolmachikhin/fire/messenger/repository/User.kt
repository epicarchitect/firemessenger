package kolmachikhin.fire.messenger.repository

import com.google.gson.JsonObject

class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

fun User.toMap() = mutableMapOf<String, Any>().apply {
    put("id", id)
    put("firstName", firstName)
    put("lastName", lastName)
    put("email", email)
}

fun JsonObject.toUser() = User(
    id = this["id"].asString,
    firstName = this["firstName"].asString,
    lastName = this["lastName"].asString,
    email = this["email"].asString
)