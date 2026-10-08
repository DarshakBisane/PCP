package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PcpColorScheme = lightColorScheme(
    primary = PcpPrimaryOrange,
    onPrimary = PcpDarkBrown,
    primaryContainer = PcpCream,
    onPrimaryContainer = PcpDarkBrown,
    secondary = PcpLightBrown,
    onSecondary = PcpBackground,
    secondaryContainer = PcpSoftOrange,
    onSecondaryContainer = PcpDarkBrown,
    tertiary = PcpDarkBrown,
    onTertiary = PcpBackground,
    background = PcpBackground,
    onBackground = PcpDarkBrown,
    surface = PcpSurface,
    onSurface = PcpDarkBrown,
    surfaceVariant = PcpSurfaceVariant,
    onSurfaceVariant = PcpMutedBrown,
    outline = PcpBorder,
    outlineVariant = PcpBorderLight,
    error = PcpError,
    onError = PcpBackground,
    errorContainer = PcpErrorContainer,
    onErrorContainer = PcpError
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PcpColorScheme,
        typography = Typography,
        content = content
    )
}
