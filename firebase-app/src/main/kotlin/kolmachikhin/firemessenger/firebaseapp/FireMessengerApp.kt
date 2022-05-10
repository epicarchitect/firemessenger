package kolmachikhin.firemessenger.firebaseapp

import android.app.Application
import kolmachikhin.firemessenger.firebaseapp.di.factoriesModule
import kolmachikhin.firemessenger.firebaseapp.di.singletonsModule
import kolmachikhin.firemessenger.presentation.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FireMessengerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@FireMessengerApp)
            modules(
                singletonsModule(),
                factoriesModule(),
                viewModelsModule()
            )
        }
    }
}