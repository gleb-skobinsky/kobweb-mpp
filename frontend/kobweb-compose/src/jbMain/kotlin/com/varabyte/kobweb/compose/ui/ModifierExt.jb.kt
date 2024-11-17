package com.varabyte.kobweb.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

actual fun Modifier.fillMaxSize(fraction: Float): Modifier = fillMaxSize(fraction)

actual fun Modifier.background(color: Color): Modifier = background(color)

actual fun Modifier.width(width: Dp): Modifier = width(width)