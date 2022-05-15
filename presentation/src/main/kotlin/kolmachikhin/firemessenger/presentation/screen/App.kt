@file:Suppress("NAME_SHADOWING")

package kolmachikhin.firemessenger.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kolmachikhin.firemessenger.presentation.R
import kolmachikhin.firemessenger.presentation.di.composeViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.app.AppState
import kolmachikhin.firemessenger.presentation.viewmodel.auth.EmailAuthorizationViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.auth.EmailRegistrationViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.chats.ChatsViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.profile.MyProfileViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.profile.ProfileViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.search.SearchViewModel
import kolmachikhin.firemessenger.repository.MyUserState

@Composable
fun App(state: AppState) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = when (state.myUserState) {
            is MyUserState.Loaded -> "chats"
            is MyUserState.NotAuthorized -> "authorization"
            else -> "loading"
        }
    ) {
        composable("chats") {
            val state by composeViewModel<ChatsViewModel>().state.collectAsState()
            Chats(
                state = state,
                navigateToProfile = {
                    navController.navigate("my_profile")
                },
                navigateToSearch = {
                    navController.navigate("search")
                }
            )
        }

        composable("my_profile") {
            val state by composeViewModel<MyProfileViewModel>().state.collectAsState()
            MyProfile(state)
        }

        composable("profile?userId={userId}") {
            val userId = it.arguments!!.getString("userId")
            val state by composeViewModel<ProfileViewModel>(userId).state.collectAsState()
            Profile(
                state = state,
                openChat = {

                }
            )
        }

        composable("loading") {
            Loading(stringResource(R.string.loading))
        }

        composable("search") {
            val state by composeViewModel<SearchViewModel>().state.collectAsState()
            Search(
                state = state,
                onSelected = {
                    navController.navigate("profile?userId=${it.id}")
                }
            )
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