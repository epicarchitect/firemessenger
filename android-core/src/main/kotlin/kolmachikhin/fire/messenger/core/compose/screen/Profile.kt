package kolmachikhin.fire.messenger.core.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kolmachikhin.fire.messenger.core.R
import kolmachikhin.fire.messenger.core.viewmodel.profile.ProfileState

@Composable
fun Profile(state: ProfileState) {
    val focusManager = LocalFocusManager.current

    when (state) {
        is ProfileState.Loaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(R.string.profile),
                    style = MaterialTheme.typography.h6
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = state.nickname,
                    onValueChange = {
                        state.updateNickname(it)
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(stringResource(R.string.nickname_input_label))
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = state.email,
                    onValueChange = {},
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(stringResource(R.string.email_input_label))
                    },
                    singleLine = true,
                    enabled = false
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 54.dp),
                    onClick = {
                        state.signOut()
                    }
                ) {
                    Text(text = stringResource(R.string.sign_out))
                }
            }
        }
        is ProfileState.Loading -> {
            Loading(stringResource(R.string.loading))
        }
    }
}