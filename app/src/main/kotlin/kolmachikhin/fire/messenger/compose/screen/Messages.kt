package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kolmachikhin.fire.messenger.viewmodel.MessagesViewModel

@Composable
fun Messages(
    viewModel: MessagesViewModel
) {
    Box {
        Text("messages")
    }
}