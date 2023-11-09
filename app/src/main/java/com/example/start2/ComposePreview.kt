package com.example.start2

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.start2.core.LocalUiMode
import com.example.start2.core.UiMode
import com.example.start2.home.ui.theme.Guardians_of_codedevelopment_mobileTheme




@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    uiMode: UiMode = UiMode.PhonePortrait,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalUiMode provides uiMode) {
        Guardians_of_codedevelopment_mobileTheme(darkTheme = darkTheme) {
            Surface() {
                content()
            }
        }
    }
}
