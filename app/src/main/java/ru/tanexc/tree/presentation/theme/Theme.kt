package ru.tanexc.tree.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.tanexc.tree.core.utils.Theme
import ru.tanexc.tree.core.utils.Theme.*
import ru.tanexc.tree.presentation.theme.Colors.AppColorScheme
import ru.tanexc.tree.presentation.theme.Colors.DefaultColorScheme
import ru.tanexc.tree.presentation.theme.Colors.GreenColorScheme
import ru.tanexc.tree.presentation.theme.Colors.OrangeColorScheme


@Composable
fun TreeTheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    rememberSystemUiController().setStatusBarColor(colorScheme.surface)
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = Typography
        )
}

fun getTheme(colorTheme: Theme, useDarkTheme: Boolean): ColorScheme {
    val scheme: AppColorScheme = when (colorTheme) {
        is Default -> DefaultColorScheme()
        is Orange -> OrangeColorScheme()
        is Purple -> GreenColorScheme()
    }

    return when (useDarkTheme) {
        true -> darkColorScheme(
            primary = scheme.md_theme_dark_primary,
            onPrimary = scheme.md_theme_dark_onPrimary,
            primaryContainer = scheme.md_theme_dark_primaryContainer,
            onPrimaryContainer = scheme.md_theme_dark_onPrimaryContainer,
            secondary = scheme.md_theme_dark_secondary,
            onSecondary = scheme.md_theme_dark_onSecondary,
            secondaryContainer = scheme.md_theme_dark_secondaryContainer,
            onSecondaryContainer = scheme.md_theme_dark_onSecondaryContainer,
            tertiary = scheme.md_theme_dark_tertiary,
            onTertiary = scheme.md_theme_dark_onTertiary,
            tertiaryContainer = scheme.md_theme_dark_tertiaryContainer,
            onTertiaryContainer = scheme.md_theme_dark_onTertiaryContainer,
            error = scheme.md_theme_dark_error,
            errorContainer = scheme.md_theme_dark_errorContainer,
            onError = scheme.md_theme_dark_onError,
            onErrorContainer = scheme.md_theme_dark_onErrorContainer,
            background = scheme.md_theme_dark_background,
            onBackground = scheme.md_theme_dark_onBackground,
            surface = scheme.md_theme_dark_surface,
            onSurface = scheme.md_theme_dark_onSurface,
            surfaceVariant = scheme.md_theme_dark_surfaceVariant,
            onSurfaceVariant = scheme.md_theme_dark_onSurfaceVariant,
            outline = scheme.md_theme_dark_outline,
            inverseOnSurface = scheme.md_theme_dark_inverseOnSurface,
            inverseSurface = scheme.md_theme_dark_inverseSurface,
            inversePrimary = scheme.md_theme_dark_inversePrimary,
            surfaceTint = scheme.md_theme_dark_surfaceTint,
            outlineVariant = scheme.md_theme_dark_outlineVariant,
            scrim = scheme.md_theme_dark_scrim,
        )

        else -> lightColorScheme(
            primary = scheme.md_theme_light_primary,
            onPrimary = scheme.md_theme_light_onPrimary,
            primaryContainer = scheme.md_theme_light_primaryContainer,
            onPrimaryContainer = scheme.md_theme_light_onPrimaryContainer,
            secondary = scheme.md_theme_light_secondary,
            onSecondary = scheme.md_theme_light_onSecondary,
            secondaryContainer = scheme.md_theme_light_secondaryContainer,
            onSecondaryContainer = scheme.md_theme_light_onSecondaryContainer,
            tertiary = scheme.md_theme_light_tertiary,
            onTertiary = scheme.md_theme_light_onTertiary,
            tertiaryContainer = scheme.md_theme_light_tertiaryContainer,
            onTertiaryContainer = scheme.md_theme_light_onTertiaryContainer,
            error = scheme.md_theme_light_error,
            errorContainer = scheme.md_theme_light_errorContainer,
            onError = scheme.md_theme_light_onError,
            onErrorContainer = scheme.md_theme_light_onErrorContainer,
            background = scheme.md_theme_light_background,
            onBackground = scheme.md_theme_light_onBackground,
            surface = scheme.md_theme_light_surface,
            onSurface = scheme.md_theme_light_onSurface,
            surfaceVariant = scheme.md_theme_light_surfaceVariant,
            onSurfaceVariant = scheme.md_theme_light_onSurfaceVariant,
            outline = scheme.md_theme_light_outline,
            inverseOnSurface = scheme.md_theme_light_inverseOnSurface,
            inverseSurface = scheme.md_theme_light_inverseSurface,
            inversePrimary = scheme.md_theme_light_inversePrimary,
            surfaceTint = scheme.md_theme_light_surfaceTint,
            outlineVariant = scheme.md_theme_light_outlineVariant,
            scrim = scheme.md_theme_light_scrim,
        )
    }
}
