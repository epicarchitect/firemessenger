package kolmachikhin.fire.messenger.core.viewmodel.chats

sealed class ChatsState {
    class Loading : ChatsState()
    data class Loaded(
        val nickname: String
    ) : ChatsState()
}