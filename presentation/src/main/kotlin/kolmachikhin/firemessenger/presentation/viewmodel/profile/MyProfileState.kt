package kolmachikhin.firemessenger.presentation.viewmodel.profile

sealed class MyProfileState {
    class Loading : MyProfileState()
    data class Loaded(
        val nickname: String,
        val email: String,
        val signOut: () -> Unit
    ) : MyProfileState()
}