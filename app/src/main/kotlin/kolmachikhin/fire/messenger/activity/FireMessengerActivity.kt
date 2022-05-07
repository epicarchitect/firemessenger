package kolmachikhin.fire.messenger.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.fire.messenger.compose.koin.composeViewModel
import kolmachikhin.fire.messenger.compose.screen.App
import kolmachikhin.fire.messenger.compose.theme.FireMessengerTheme

class FireMessengerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FireMessengerTheme {
                Surface {
                    App(
                        appViewModel = composeViewModel()
                    )
                }
            }
        }
    }
}