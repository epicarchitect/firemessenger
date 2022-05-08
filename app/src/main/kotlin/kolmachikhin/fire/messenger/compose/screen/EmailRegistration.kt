package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import kolmachikhin.fire.messenger.authorization.EmailRegistrar
import kolmachikhin.fire.messenger.validation.*
import kolmachikhin.fire.messenger.viewmodel.EmailRegistrationViewModel

@Composable
fun EmailRegistration(emailRegistrationViewModel: EmailRegistrationViewModel) {
    val emailRegistrationState by emailRegistrationViewModel.state.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = emailRegistrationState) {
            is EmailRegistrationViewModel.State.Input -> {
                val isFirstNameIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedFirstName is Incorrect
                val isLastNameIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedLastName is Incorrect
                val isEmailIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedEmail is Incorrect
                val isPasswordIncorrect = state is EmailRegistrationViewModel.State.Input.Incorrect && state.validatedPassword is Incorrect

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(R.drawable.ic_launcher),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = stringResource(R.string.app_name),
                            color = colorResource(R.color.primary),
                            style = MaterialTheme.typography.h5
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp),
                        value = state.firstName,
                        onValueChange = {
                            state.updateFirstName(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = {
                            Text(stringResource(R.string.firstName_input_label))
                        },
                        singleLine = true
                    )

                    AnimatedVisibility(visible = isFirstNameIncorrect) {
                        val incorrectFirstName = (state as? EmailRegistrationViewModel.State.Input.Incorrect)?.validatedFirstName as? Incorrect
                        if (incorrectFirstName != null) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 54.dp, end = 54.dp, top = 4.dp),
                                text = when (val reason = incorrectFirstName.reason) {
                                    is FirstNameValidator.IncorrectReason.Empty -> stringResource(R.string.incorrect_firstName_input_empty)
                                    is FirstNameValidator.IncorrectReason.TooLong -> stringResource(R.string.incorrect_firstName_input_tooLong, reason.maxNameLength)
                                },
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp),
                        value = state.lastName,
                        onValueChange = {
                            state.updateLastName(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = {
                            Text(stringResource(R.string.lastName_input_label))
                        },
                        singleLine = true
                    )

                    AnimatedVisibility(visible = isLastNameIncorrect) {
                        val incorrectLastName = (state as? EmailRegistrationViewModel.State.Input.Incorrect)?.validatedLastName as? Incorrect
                        if (incorrectLastName != null) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 54.dp, end = 54.dp, top = 4.dp),
                                text = when (val reason = incorrectLastName.reason) {
                                    is LastNameValidator.IncorrectReason.Empty -> stringResource(R.string.incorrect_lastName_input_empty)
                                    is LastNameValidator.IncorrectReason.TooLong -> stringResource(R.string.incorrect_lastName_input_tooLong, reason.maxNameLength)
                                },
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp),
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
                                    .padding(start = 54.dp, end = 54.dp, top = 4.dp),
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
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp),
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
                                    .padding(start = 54.dp, end = 54.dp, top = 4.dp),
                                text = when (val reason = incorrectPassword.reason) {
                                    is PasswordValidator.IncorrectReason.Empty -> stringResource(R.string.incorrect_password_input_empty)
                                    is PasswordValidator.IncorrectReason.Short -> stringResource(R.string.incorrect_password_input_short, reason.minPasswordLength)
                                },
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 54.dp, end = 54.dp, top = 16.dp),
                        onClick = {
                            (state as? EmailRegistrationViewModel.State.Input.Correct)?.startRegistration?.invoke()
                        },
                        enabled = state is EmailRegistrationViewModel.State.Input.Correct
                    ) {
                        Text(stringResource(R.string.register_button))
                    }
                }
            }
            is EmailRegistrationViewModel.State.Loading -> {
                Loading(stringResource(R.string.registration))
            }
            is EmailRegistrationViewModel.State.RegistrationFailed -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 54.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (val result = state.result) {
                            is EmailRegistrar.Result.Failed.EmailAlreadyUsed -> {
                                stringResource(R.string.registration_error_email_already_used, result.email)
                            }
                            is EmailRegistrar.Result.Failed.Unknown -> {
                                stringResource(R.string.registration_error_unknown)
                            }
                            is EmailRegistrar.Result.Failed.ConnectionError -> {
                                stringResource(R.string.registration_error_connection)
                            }
                        }
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        onClick = { state.retry() }
                    ) {
                        Text(stringResource(R.string.retry_registration_button))
                    }
                }
            }
            is EmailRegistrationViewModel.State.RegistrationSuccess -> {
                /* no-op */
            }
        }
    }
}