package kolmachikhin.firemessenger.firebaseapp.mapper

import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.google.gson.JsonObject
import kolmachikhin.firemessenger.data.Chat
import kolmachikhin.firemessenger.data.UserData
import kotlinx.datetime.Instant

fun DataSnapshot.toChat() = Gson()
    .fromJson(value.toString(), JsonObject::class.java)
    .toChat()

fun Chat.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "messages" to messages.toMap()
)

fun JsonObject.toChat() = Chat(
    id = this["id"].asString,
    name = this["name"].asString,
    messages = this["messages"].asJsonArray.map {
        it.asJsonObject.toMessage()
    }
)

fun List<Chat.Message>.toMap() = associate {
    it.id to it.toMap()
}

fun Chat.Message.toMap() = mapOf(
    "id" to id,
    "userId" to userId,
    "text" to text,
    "time" to time
)

fun JsonObject.toMessage() = Chat.Message(
    id = this["id"].asString,
    userId = this["userId"].asString,
    text = this["text"].asString,
    time = Instant.parse(this["time"].asString),
)