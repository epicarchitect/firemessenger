package kolmachikhin.fire.messenger.compose.screen.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolmachikhin.fire.messenger.R
import kolmachikhin.fire.messenger.compose.ext.composeViewModel
import kolmachikhin.fire.messenger.compose.screen.profile.Profile
import kolmachikhin.fire.messenger.compose.screen.chats.Chats
import kolmachikhin.fire.messenger.compose.screen.auth.EmailAuthorization
import kolmachikhin.fire.messenger.compose.screen.auth.EmailRegistration
import kolmachikhin.fire.messenger.compose.screen.loading.Loading
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.viewmodel.app.AppState
import kolmachikhin.fire.messenger.viewmodel.auth.EmailAuthorizationViewModel
import kolmachikhin.fire.messenger.viewmodel.auth.EmailRegistrationViewModel
import kolmachikhin.fire.messenger.viewmodel.profile.ProfileViewModel

@Composable
fun App(state: AppState) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = when (state.userState) {
            is UserRepository.State.Loaded -> "chats"
            is UserRepository.State.NotAuthorized -> "authorization"
            else -> "loading"
        }
    ) {
        composable("chats") {
            Chats(
                navigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            val state by composeViewModel<ProfileViewModel>().state.collectAsState()
            Profile(state)
        }

        composable("loading") {
            Loading(stringResource(R.string.loading))
        }

        composable("authorization") {
            val state by composeViewModel<EmailAuthorizationViewModel>().state.collectAsState()
            EmailAuthorization(
                state = state,
                navigateToRegistration = {
                    navController.navigate("registration")
                }
            )
        }

        composable("registration") {
            val state by composeViewModel<EmailRegistrationViewModel>().state.collectAsState()
            EmailRegistration(state)
        }
    }
}