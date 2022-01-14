package com.che.zadali.sgo_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorPalette = lightColors(
    primary = PrimaryBlue,
    primaryVariant = MainTextColor,
    secondary = CaptionTextBlue,
    onError = SecondGradeColor,
    background = BackgroundColor,
    onBackground = PrimaryBlue
)

private val DarkColorPalette = darkColors(
    primary = DarkPrimaryBlue,
    primaryVariant = DarkMainTextColor,
    secondary = DarkCaptionTextBlue,
    onError = DarkSecondGradeColor,
    background = DarkBackgroundColor,
    onBackground = DarkPrimaryBlue
)

@Composable
fun SgoAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    if (darkTheme) {
        rememberSystemUiController().setStatusBarColor(DarkBackgroundColor)
        MaterialTheme(
            colors = DarkColorPalette,
            typography = DarkTypography,
            shapes = Shapes,
            content = content
        )
    } else {
        rememberSystemUiController().setStatusBarColor(BackgroundColor)
        MaterialTheme(
            colors = LightColorPalette,
            typography = LightTypography,
            shapes = Shapes,
            content = content
        )
    }

}