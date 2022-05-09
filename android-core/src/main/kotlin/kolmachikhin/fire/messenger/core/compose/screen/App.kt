@file:Suppress("NAME_SHADOWING")

package kolmachikhin.fire.messenger.core.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolmachikhin.fire.messenger.core.R
import kolmachikhin.fire.messenger.core.compose.ext.composeViewModel
import kolmachikhin.fire.messenger.repository.CurrentUserRepository
import kolmachikhin.fire.messenger.core.viewmodel.app.AppState
import kolmachikhin.fire.messenger.core.viewmodel.auth.EmailAuthorizationViewModel
import kolmachikhin.fire.messenger.core.viewmodel.auth.EmailRegistrationViewModel
import kolmachikhin.fire.messenger.core.viewmodel.chats.ChatsViewModel
import kolmachikhin.fire.messenger.core.viewmodel.profile.ProfileViewModel
import kolmachikhin.fire.messenger.core.viewmodel.search.SearchViewModel
import kolmachikhin.fire.messenger.repository.CurrentUserState

@Composable
fun App(state: AppState) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = when (state.currentUserState) {
            is CurrentUserState.Loaded -> "chats"
            is CurrentUserState.NotAuthorized -> "authorization"
            else -> "loading"
        }
    ) {
        composable("chats") {
            val state by composeViewModel<ChatsViewModel>().state.collectAsState()
            Chats(
                state = state,
                navigateToProfile = {
                    navController.navigate("profile")
                },
                navigateToSearch = {
                    navController.navigate("search")
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

        composable("search") {
            val state by composeViewModel<SearchViewModel>().state.collectAsState()
            Search(state)
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