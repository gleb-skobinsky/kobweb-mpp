package com.varabyte.kobweb.compose.ui

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.*
import androidx.compose.ui.graphics.Color as ComposeColor

actual fun Modifier.fillMaxSize(fraction: Float): Modifier = fillMaxSize(fraction.toPercent())

actual fun Modifier.background(color: ComposeColor): Modifier = backgroundColor(color.toCssColor())

fun Float.toPercent(): CSSPercentageNumericValue {
    if (0 >= this) return 0.percent
    if (this >= 1) return 100.percent
    return (this * 100).percent
}

@Stable
fun ComposeColor.toCssColor(): CSSColorValue = rgba(
    r = red.take255(),
    g = green.take255(),
    b = blue.take255(),
    a = alpha.take255()
)

inline fun Float.take255() = this * 255