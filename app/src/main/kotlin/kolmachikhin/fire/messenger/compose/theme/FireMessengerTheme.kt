package kolmachikhin.fire.messenger.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import kolmachikhin.fire.messenger.R
import androidx.compose.material.darkColors as materialDarkColors
import androidx.compose.material.lightColors as materialLightColors

@Composable
fun darkColors() = materialDarkColors(
    primary = colorResource(R.color.primary)
)

@Composable
fun lightColors() = materialLightColors(
    primary = colorResource(R.color.primary)
)

@Composable
fun FireMessengerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (false) darkColors() else lightColors(),
        content = content
    )
}