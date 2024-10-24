package com.varabyte.kobweb.material3

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.KobwebComposeStyles
import com.varabyte.kobweb.core.KobwebApp
import com.varabyte.kobweb.material3.init.SilkWidgetVariables

@Composable
fun SilkApp(content: @Composable () -> Unit) {
    KobwebApp {
        KobwebComposeStyles()
        SilkFoundationStyles()
        SilkWidgetVariables()
        content()
    }
}
