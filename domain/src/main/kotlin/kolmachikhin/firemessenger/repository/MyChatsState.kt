package kolmachikhin.firemessenger.repository

import kolmachikhin.firemessenger.data.Chat

sealed class MyChatsState {
    class Loading : MyChatsState()
    class Loaded(val chats: List<Chat>) : MyChatsState()
}