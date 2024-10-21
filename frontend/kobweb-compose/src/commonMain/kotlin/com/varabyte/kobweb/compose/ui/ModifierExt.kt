package com.varabyte.kobweb.compose.ui

import androidx.compose.ui.graphics.Color

expect fun Modifier.fillMaxSize(fraction: Float = 1f): Modifier

expect fun Modifier.background(color: Color): Modifier