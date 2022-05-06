package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.fire.messenger.viewmodel.RegistrationViewModel

@Composable
fun Registration(
    registrationViewModel: RegistrationViewModel
) {
    val registrationState by registrationViewModel.state.collectAsState()

    Box {
        when (val state = registrationState) {
            is RegistrationViewModel.State.Input -> {
                Column {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = {
                            registrationViewModel.setEmail(it)
                        }
                    )

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            registrationViewModel.setPassword(it)
                        }
                    )

                    Button(onClick = { registrationViewModel.register() }) {
                        Text("Register")
                    }
                }
            }
            is RegistrationViewModel.State.Registered -> {
                Text("Registered")
            }
            is RegistrationViewModel.State.Registration -> {
                CircularProgressIndicator()
            }
            is RegistrationViewModel.State.RegistrationFailed -> {
                Text("Failed")
            }
        }
    }
}