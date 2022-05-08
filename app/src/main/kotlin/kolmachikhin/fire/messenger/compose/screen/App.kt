package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolmachikhin.fire.messenger.R
import kolmachikhin.fire.messenger.compose.koin.composeViewModel
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.viewmodel.AppViewModel

@Composable
fun App(
    appViewModel: AppViewModel
) {
    val navController = rememberNavController()
    val appState by appViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = when (appState.userState) {
            is UserRepository.UserState.Loaded -> "chats"
            is UserRepository.UserState.NotAuthorized -> "authorization"
            else -> "loading"
        }
    ) {
        composable("chats") {
            Chats(
                chatsViewModel = composeViewModel(),
                navigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            Profile(profileViewModel = composeViewModel())
        }

        composable("loading") {
            Loading(stringResource(R.string.loading))
        }

        composable("authorization") {
            EmailAuthorization(
                emailAuthorizationViewModel = composeViewModel(),
                navigateToRegistration = {
                    navController.navigate("registration")
                }
            )
        }

        composable("registration") {
            EmailRegistration(
                emailRegistrationViewModel = composeViewModel()
            )
        }
    }
}