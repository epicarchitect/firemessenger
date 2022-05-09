package kolmachikhin.fire.messenger

import android.app.Application
import kolmachikhin.fire.messenger.auth.EmailAuthorizer
import kolmachikhin.fire.messenger.auth.EmailRegistrar
import kolmachikhin.fire.messenger.auth.FirebaseEmailAuthorizer
import kolmachikhin.fire.messenger.auth.FirebaseEmailRegistrar
import kolmachikhin.fire.messenger.repository.FirebaseCurrentUserRepository
import kolmachikhin.fire.messenger.repository.CurrentUserRepository
import kolmachikhin.fire.messenger.validation.EmailValidator
import kolmachikhin.fire.messenger.validation.NicknameValidator
import kolmachikhin.fire.messenger.validation.PasswordValidator
import kolmachikhin.fire.messenger.core.viewmodel.app.AppViewModel
import kolmachikhin.fire.messenger.core.viewmodel.auth.EmailAuthorizationViewModel
import kolmachikhin.fire.messenger.core.viewmodel.auth.EmailRegistrationViewModel
import kolmachikhin.fire.messenger.core.viewmodel.chats.ChatsViewModel
import kolmachikhin.fire.messenger.core.viewmodel.profile.ProfileViewModel
import kolmachikhin.fire.messenger.core.viewmodel.search.SearchViewModel
import kolmachikhin.fire.messenger.repository.FirebaseUsersRepository
import kolmachikhin.fire.messenger.repository.UsersRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class FireMessengerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@FireMessengerApp)
            modules(
                module(createdAtStart = true) {
                    single<CurrentUserRepository> { FirebaseCurrentUserRepository() }
                    single<UsersRepository> { FirebaseUsersRepository() }
                },
                module {
                    factory<EmailAuthorizer> { FirebaseEmailAuthorizer() }
                    factory<EmailRegistrar> { FirebaseEmailRegistrar() }
                    factory { NicknameValidator(maxNicknameLength = 25, minNicknameLength = 3) }
                    factory { PasswordValidator(minPasswordLength = 6) }
                    factory { EmailValidator() }

                    viewModel { ChatsViewModel(get()) }
                    viewModel { ProfileViewModel(get()) }
                    viewModel { EmailRegistrationViewModel(get(), get(), get(), get()) }
                    viewModel { EmailAuthorizationViewModel(get(), get(), get()) }
                    viewModel { AppViewModel(get()) }
                    viewModel { SearchViewModel(get()) }
                }
            )
        }
    }
}