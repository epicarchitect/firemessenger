package kolmachikhin.fire.messenger

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kolmachikhin.fire.messenger.authorization.EmailAuthorizer
import kolmachikhin.fire.messenger.authorization.EmailRegistrar
import kolmachikhin.fire.messenger.authorization.FirebaseEmailAuthorizer
import kolmachikhin.fire.messenger.authorization.FirebaseEmailRegistrar
import kolmachikhin.fire.messenger.repository.UserMessagesRepository
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.validation.EmailValidator
import kolmachikhin.fire.messenger.validation.FirstNameValidator
import kolmachikhin.fire.messenger.validation.LastNameValidator
import kolmachikhin.fire.messenger.validation.PasswordValidator
import kolmachikhin.fire.messenger.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                    single { Firebase.auth }
                    single { Firebase.database }
                    single { Gson() }
                    single { UserRepository(CoroutineScope(Dispatchers.IO), get(), get(), get()) }
                    single { UserMessagesRepository() }
                },
                module {
                    factory<EmailAuthorizer> { FirebaseEmailAuthorizer(get()) }
                    factory<EmailRegistrar> { FirebaseEmailRegistrar(get(), get()) }
                    factory { FirstNameValidator(maxNameLength = 25) }
                    factory { LastNameValidator(maxNameLength = 25) }
                    factory { EmailValidator() }
                    factory { PasswordValidator(minPasswordLength = 6) }

                    viewModel { ProfileViewModel(get()) }
                    viewModel { ChatsViewModel() }
                    viewModel { EmailRegistrationViewModel(get(), get(), get(), get(), get()) }
                    viewModel { EmailAuthorizationViewModel(get(), get(), get()) }
                    viewModel { AppViewModel(get()) }
                }
            )
        }
    }
}