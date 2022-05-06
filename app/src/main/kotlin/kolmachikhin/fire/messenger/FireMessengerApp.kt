package kolmachikhin.fire.messenger

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.fire.messenger.repository.AuthorizationRepository
import kolmachikhin.fire.messenger.repository.RegistrationRepository
import kolmachikhin.fire.messenger.repository.UserMessagesRepository
import kolmachikhin.fire.messenger.repository.UserRepository
import kolmachikhin.fire.messenger.viewmodel.LoginViewModel
import kolmachikhin.fire.messenger.viewmodel.MessagesViewModel
import kolmachikhin.fire.messenger.viewmodel.ProfileViewModel
import kolmachikhin.fire.messenger.viewmodel.RegistrationViewModel
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
                module {
                    single { Firebase.auth }
                    single { Firebase.database }
                    single { UserRepository(CoroutineScope(Dispatchers.IO), get(), get()) }
                    single { UserMessagesRepository() }
                    single { AuthorizationRepository(get()) }
                    single { RegistrationRepository(get()) }
                    viewModel { ProfileViewModel(get()) }
                    viewModel { MessagesViewModel() }
                    viewModel { RegistrationViewModel(get()) }
                    viewModel { LoginViewModel() }
                }
            )
        }
    }
}