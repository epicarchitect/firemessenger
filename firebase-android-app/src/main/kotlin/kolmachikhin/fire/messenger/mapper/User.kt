package kolmachikhin.fire.messenger.mapper

import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.google.gson.JsonObject
import kolmachikhin.fire.messenger.data.User

fun DataSnapshot.toUser() = Gson()
    .fromJson(value.toString(), JsonObject::class.java)
    .toUser()

fun User.toMap() = mutableMapOf<String, Any>().apply {
    put("id", id)
    put("nickname", nickname)
    put("email", email)
}

fun JsonObject.toUser() = User(
    id = this["id"].asString,
    nickname = this["nickname"].asString,
    email = this["email"].asString
)