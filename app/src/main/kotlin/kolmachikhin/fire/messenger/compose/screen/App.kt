package kolmachikhin.fire.messenger.compose.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kolmachikhin.fire.messenger.R
import kolmachikhin.fire.messenger.compose.koin.composeViewModel
import kolmachikhin.fire.messenger.viewmodel.AppViewModel

private class BottomTab(
    val route: String,
    val titleResourceId: Int,
    val iconResourceId: Int,
)

private val bottomTabs = listOf(
    BottomTab("messages", R.string.messages, R.drawable.ic_messages),
    BottomTab("profile", R.string.profile, R.drawable.ic_profile)
)

@Composable
fun App(
    appViewModel: AppViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val appState by appViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = appState.currentScreen) {
        composable("loading") {
            CircularProgressIndicator()
        }

        composable("email_registration") {
            EmailRegistration(
                emailRegistrationViewModel = composeViewModel()
            )
        }

        composable("main") {
            Main()
        }
    }
}
@Composable
fun Main() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val currentDestination = navBackStackEntry?.destination
                bottomTabs.forEach { tab ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(tab.iconResourceId),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(stringResource(tab.titleResourceId))
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "messages"
        ) {
            composable("messages") {
                Messages(viewModel = composeViewModel())
            }

            composable("profile") {
                Profile(viewModel = composeViewModel())
            }
        }
    }
}