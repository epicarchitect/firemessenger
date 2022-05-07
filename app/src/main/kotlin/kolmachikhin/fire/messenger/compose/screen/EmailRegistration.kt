package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kolmachikhin.fire.messenger.R
import kolmachikhin.fire.messenger.validation.EmailValidator
import kolmachikhin.fire.messenger.validation.Incorrect
import kolmachikhin.fire.messenger.validation.PasswordValidator
import kolmachikhin.fire.messenger.viewmodel.EmailRegistrationViewModel

@Composable
fun EmailRegistration(
    emailRegistrationViewModel: EmailRegistrationViewModel
) {
    val emailRegistrationState by emailRegistrationViewModel.state.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = emailRegistrationState) {
            is EmailRegistrationViewModel.State.Input -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(54.dp)
                ) {

                    Image(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.app_name),
                        color = colorResource(R.color.primary),
                        style = MaterialTheme.typography.h5
                    )

                    val isEmailIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedEmail is Incorrect
                    val isPasswordIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedPassword is Incorrect

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        value = state.email,
                        onValueChange = {
                            state.updateEmail(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        label = {
                            Text(stringResource(R.string.email_input_label))
                        },
                        isError = isEmailIncorrect,
                        singleLine = true
                    )

                    AnimatedVisibility(visible = isEmailIncorrect) {
                        val incorrectEmail = (state as? EmailRegistrationViewModel.State.Input.Incorrect)?.validatedEmail as? Incorrect
                        if (incorrectEmail != null) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                text = when (incorrectEmail.reason) {
                                    is EmailValidator.IncorrectReason.Empty -> stringResource(R.string.incorrect_email_input_empty)
                                    is EmailValidator.IncorrectReason.Pattern -> stringResource(R.string.incorrect_email_input_pattern)
                                },
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        value = state.password,
                        onValueChange = {
                            state.updatePassword(it)
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        label = {
                            Text(stringResource(R.string.password_input_label))
                        },
                        isError = isPasswordIncorrect,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )


                    AnimatedVisibility(visible = isPasswordIncorrect) {
                        val incorrectPassword = (state as? EmailRegistrationViewModel.State.Input.Incorrect)?.validatedPassword as? Incorrect
                        if (incorrectPassword != null) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                text = when (val reason = incorrectPassword.reason) {
                                    is PasswordValidator.IncorrectReason.Empty -> stringResource(R.string.incorrect_password_input_empty)
                                    is PasswordValidator.IncorrectReason.Short -> stringResource(R.string.incorrect_password_input_short, reason.minPasswordLength)
                                },
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    AnimatedVisibility(visible = state is EmailRegistrationViewModel.State.Input.Correct) {
                        val correctInput = state as? EmailRegistrationViewModel.State.Input.Correct
                        if (correctInput != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                onClick = { correctInput.startRegistration() }
                            ) {
                                Text(stringResource(R.string.register_button))
                            }
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