package kolmachikhin.firemessenger.data

import kotlinx.datetime.Instant

typealias ChatId = String
typealias ChatName = String
typealias MessageId = String
typealias MessageText = String

data class Chat(
    val id: ChatId,
    val name: ChatName,
    val messages: List<Message>
) {

    data class Message(
        val id: MessageId,
        val userId: UserId,
        val text: MessageText,
        val time: Instant
    )
}