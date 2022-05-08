package kolmachikhin.fire.messenger.core.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kolmachikhin.fire.messenger.core.R

@Composable
fun Chats(
    navigateToProfile: () -> Unit
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
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_launcher),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h6
                    )
                }

                IconButton(
                    onClick = navigateToProfile
                ) {
                    Icon(Icons.Default.Person, null)
                }
            }

        }
    }
}