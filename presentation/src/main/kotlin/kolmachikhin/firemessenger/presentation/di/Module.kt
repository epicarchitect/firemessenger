package kolmachikhin.firemessenger.presentation.di

import kolmachikhin.firemessenger.presentation.viewmodel.app.AppViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.auth.EmailAuthorizationViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.auth.EmailRegistrationViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.chats.ChatsViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.profile.ProfileViewModel
import kolmachikhin.firemessenger.presentation.viewmodel.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun viewModelsModule() = module {
    viewModel { ChatsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EmailRegistrationViewModel(get(), get(), get(), get()) }
    viewModel { EmailAuthorizationViewModel(get(), get(), get()) }
    viewModel { AppViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}