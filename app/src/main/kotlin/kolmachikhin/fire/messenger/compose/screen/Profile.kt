package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.viewmodel.ProfileViewModel

@Composable
fun Profile(
    viewModel: ProfileViewModel
) {
    Column {
        val user by viewModel.user.collectAsState()

        when (val state = user) {
            is UserRepository.UserState.Authorizing -> {


            }
            is UserRepository.UserState.Loaded -> {
                Text("user: $${state.user}")
            }
            is UserRepository.UserState.Loading -> {

            }
            is UserRepository.UserState.NotAuthorized -> {

            }
        }

        Button(onClick = {
            viewModel.signOut()
        }) {
            Text("signOut")
        }
    }
}