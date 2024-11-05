package com.varabyte.kobweb.compose.ui

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

expect fun Modifier.fillMaxSize(fraction: Float = 1f): Modifier

expect fun Modifier.background(color: Color): Modifier

@Stable
inline fun <T> Modifier.applyNullable(
    value: T?,
    applier: Modifier.(T) -> Modifier
): Modifier = value?.let { this.applier(it) } ?: this

@Stable
inline fun Modifier.applyIf(condition: Boolean, lazyProduce: Modifier.() -> Modifier): Modifier {
    return if (condition) this.lazyProduce() else this
}