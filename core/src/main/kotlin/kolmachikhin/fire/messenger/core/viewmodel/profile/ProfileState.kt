package kolmachikhin.fire.messenger.core.viewmodel.profile

sealed class ProfileState {
    class Loading : ProfileState()
    data class Loaded(
        val nickname: String,
        val email: String,
        val updateNickname: (String) -> Unit,
        val signOut: () -> Unit
    ) : ProfileState()
}