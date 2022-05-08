package kolmachikhin.fire.messenger.mapper

import com.google.gson.JsonObject
import kolmachikhin.fire.messenger.data.User

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