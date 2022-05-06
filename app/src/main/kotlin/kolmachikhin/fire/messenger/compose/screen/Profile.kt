package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.fire.messenger.viewmodel.ProfileViewModel

@Composable
fun Profile(
    viewModel: ProfileViewModel
) {
    Box {
        val user by viewModel.user.collectAsState()

        Text("user: $user")

        Button(onClick = {
            viewModel.signOut()
        }) {
            Text("signOut")
        }
    }
}