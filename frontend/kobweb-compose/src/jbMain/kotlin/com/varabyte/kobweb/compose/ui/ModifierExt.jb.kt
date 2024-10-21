package com.varabyte.kobweb.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color

actual fun Modifier.fillMaxSize(fraction: Float): Modifier = fillMaxSize(fraction)

actual fun Modifier.background(color: Color): Modifier = background(color)