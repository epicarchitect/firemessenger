package kolmachikhin.fire.messenger.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.fire.messenger.compose.ext.composeViewModel
import kolmachikhin.fire.messenger.compose.screen.app.App
import kolmachikhin.fire.messenger.compose.theme.FireMessengerTheme
import kolmachikhin.fire.messenger.viewmodel.app.AppViewModel

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