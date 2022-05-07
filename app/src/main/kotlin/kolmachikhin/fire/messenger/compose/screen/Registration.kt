package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import kolmachikhin.fire.messenger.validation.EmailValidator
import kolmachikhin.fire.messenger.validation.Incorrect
import kolmachikhin.fire.messenger.validation.PasswordValidator
import kolmachikhin.fire.messenger.viewmodel.EmailRegistrationViewModel

@Composable
fun Registration(
    emailRegistrationViewModel: EmailRegistrationViewModel
) {
    val emailRegistrationState by emailRegistrationViewModel.state.collectAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        when (val state = emailRegistrationState) {
            is EmailRegistrationViewModel.State.Input -> {
                Column {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = {
                            state.updateEmail(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email
                        )
                    )

                    if (state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedEmail is Incorrect) {
                        Text(
                            text = when (state.validatedEmail.reason) {
                                is EmailValidator.IncorrectReason.Empty -> "Empty email"
                                is EmailValidator.IncorrectReason.Pattern -> "Incorrect pattern"
                            }
                        )
                    }

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            state.updatePassword(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    if (state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedPassword is Incorrect) {
                        Text(
                            text = when (state.validatedPassword.reason) {
                                is PasswordValidator.IncorrectReason.Empty -> "Password cant be empty"
                                is PasswordValidator.IncorrectReason.Short -> "Password too short"
                            }
                        )
                    }

                    if (state is EmailRegistrationViewModel.State.Input.Correct) {
                        Button(onClick = { state.startRegistration() }) {
                            Text("Register")
                        }
                    }
                }
            }
            is EmailRegistrationViewModel.State.Loading -> {
                CircularProgressIndicator()
            }
            is EmailRegistrationViewModel.State.RegistrationFailed -> {
                Column {
                    Text("Registration Failed")
                    Button(onClick = { state.retry() }) {
                        Text("Retry")
                    }
                }
            }
            is EmailRegistrationViewModel.State.RegistrationSuccess -> {
                Text("Registration Success")
            }
        }
    }
}