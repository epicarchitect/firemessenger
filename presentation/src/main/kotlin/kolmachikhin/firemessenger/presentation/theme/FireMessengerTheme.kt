package kolmachikhin.firemessenger.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import kolmachikhin.firemessenger.presentation.R
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
        colors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
        content = content
    )
}