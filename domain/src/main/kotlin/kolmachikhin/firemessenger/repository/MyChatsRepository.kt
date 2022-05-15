package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.Chat
import kolmachikhin.firemessenger.data.ChatId
import kolmachikhin.firemessenger.data.UserId
import kotlinx.coroutines.flow.StateFlow

interface MyChatsRepository {

    val state: StateFlow<MyChatsState>

    suspend fun createChatWithUser(userId: UserId): ChatId

    suspend fun sendMessage(chatId: ChatId, message: Chat.Message)

}