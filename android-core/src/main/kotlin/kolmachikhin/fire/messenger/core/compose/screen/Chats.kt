package kolmachikhin.fire.messenger.core.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kolmachikhin.fire.messenger.core.viewmodel.chats.ChatsState

@Composable
fun Chats(
    state: ChatsState,
    navigateToProfile: () -> Unit,
    navigateToSearch: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = navigateToProfile
                    ) {
                        Icon(Icons.Default.Person, null)
                    }

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (state is ChatsState.Loaded) {
                            state.nickname
                        } else {
                            ""
                        },
                        style = MaterialTheme.typography.h6
                    )
                }

                IconButton(
                    onClick = {
                        navigateToSearch()
                    }
                ) {
                    Icon(Icons.Default.Search, null)
                }
            }
        }
    }
}