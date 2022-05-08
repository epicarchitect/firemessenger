package kolmachikhin.fire.messenger.core.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.fire.messenger.core.compose.ext.composeViewModel
import kolmachikhin.fire.messenger.core.compose.screen.App
import kolmachikhin.fire.messenger.core.compose.theme.FireMessengerTheme
import kolmachikhin.fire.messenger.core.viewmodel.app.AppViewModel

class FireMessengerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FireMessengerTheme {
                Surface {
                    val state by composeViewModel<AppViewModel>().state.collectAsState()
                    App(state)
                }
            }
        }
    }
}