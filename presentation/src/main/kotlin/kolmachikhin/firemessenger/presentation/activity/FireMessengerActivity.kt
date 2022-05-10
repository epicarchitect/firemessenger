package kolmachikhin.firemessenger.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kolmachikhin.firemessenger.presentation.di.composeViewModel
import kolmachikhin.firemessenger.presentation.screen.App
import kolmachikhin.firemessenger.presentation.theme.FireMessengerTheme
import kolmachikhin.firemessenger.presentation.viewmodel.app.AppViewModel

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