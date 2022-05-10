package kolmachikhin.firemessenger.firebaseapp.mapper

import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.google.gson.JsonObject
import kolmachikhin.firemessenger.data.UserData

fun DataSnapshot.toUser() = Gson()
    .fromJson(value.toString(), JsonObject::class.java)
    .toUser()

fun UserData.toMap() = mutableMapOf<String, Any>().apply {
    put("id", id)
    put("nickname", nickname)
    put("email", email)
}

fun JsonObject.toUser() = UserData(
    id = this["id"].asString,
    nickname = this["nickname"].asString,
    email = this["email"].asString
)