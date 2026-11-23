package com.example.studentstudyplanner.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNavyBlue,
    onPrimary = White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DarkNavyText,
    secondary = BrightBlue,
    onSecondary = White,
    secondaryContainer = SoftLavender,
    onSecondaryContainer = DarkNavyText,
    tertiary = MintGreen,
    onTertiary = DarkNavyText,
    tertiaryContainer = MintGreen,
    onTertiaryContainer = DarkNavyText,
    error = ErrorRed,
    onError = White,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavyBlue,
    onPrimary = White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DarkNavyText,
    secondary = BrightBlue,
    onSecondary = White,
    secondaryContainer = SoftLavender,
    onSecondaryContainer = DarkNavyText,
    tertiary = MintGreen,
    onTertiary = DarkNavyText,
    tertiaryContainer = MintGreen,
    onTertiaryContainer = DarkNavyText,
    error = ErrorRed,
    onError = White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = ErrorRed,
    background = CoolWhite,
    onBackground = DarkNavyText,
    surface = White,
    onSurface = DarkNavyText,
    surfaceVariant = White,
    onSurfaceVariant = SecondaryGray
)

@Composable
fun StudentStudyPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
