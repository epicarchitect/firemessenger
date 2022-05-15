package kolmachikhin.firemessenger.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(24.dp),
            large = RoundedCornerShape(24.dp),
        )
    ) {
        Surface(content = content)
    }
}