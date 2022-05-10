package kolmachikhin.firemessenger.presentation.viewmodel.chats

sealed class ChatsState {
    class Loading : ChatsState()
    data class Loaded(
        val nickname: String
    ) : ChatsState()
}