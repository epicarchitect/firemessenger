package kolmachikhin.firemessenger.presentation.viewmodel.profile

sealed class ProfileState {
    class Loading : ProfileState()
    data class Loaded(
        val nickname: String,
        val email: String
    ) : ProfileState()
}