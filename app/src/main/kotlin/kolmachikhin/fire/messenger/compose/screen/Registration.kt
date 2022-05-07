package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = emailRegistrationState) {
            is EmailRegistrationViewModel.State.Input -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.email,
                        onValueChange = {
                            state.updateEmail(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email
                        ),
                        label = {
                            Text("Email")
                        },
                        isError = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedEmail is Incorrect
                    )

                    if (state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedEmail is Incorrect) {
                        Text(
                            text = when (state.validatedEmail.reason) {
                                is EmailValidator.IncorrectReason.Empty -> "Empty email"
                                is EmailValidator.IncorrectReason.Pattern -> "Incorrect pattern"
                            },
                            color = MaterialTheme.colors.error
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        value = state.password,
                        onValueChange = {
                            state.updatePassword(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        ),
                        label = {
                            Text("Password")
                        },
                        isError = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedPassword is Incorrect
                    )

                    if (state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedPassword is Incorrect) {
                        Text(
                            text = when (val reason = state.validatedPassword.reason) {
                                is PasswordValidator.IncorrectReason.Empty -> "Password cant be empty"
                                is PasswordValidator.IncorrectReason.Short -> "Password cant be small then ${reason.minPasswordLength}"
                            },
                            color = MaterialTheme.colors.error
                        )
                    }

                    if (state is EmailRegistrationViewModel.State.Input.Correct) {
                        Button(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            onClick = { state.startRegistration() }
                        ) {
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