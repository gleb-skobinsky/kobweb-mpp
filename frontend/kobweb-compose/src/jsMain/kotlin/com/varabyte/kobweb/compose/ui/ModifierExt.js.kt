package com.varabyte.kobweb.compose.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import com.varabyte.kobweb.compose.css.CSSPercentageNumericValue
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import androidx.compose.ui.graphics.Color as ComposeColor

actual fun Modifier.fillMaxSize(fraction: Float): Modifier = fillMaxSize(fraction.toPercent())

actual fun Modifier.background(color: ComposeColor): Modifier = backgroundColor(color.toCssColor())

actual fun Modifier.width(width: Dp): Modifier = width(width.value.px)

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

fun JsModifier.lineClamp(maxLines: Int): Modifier = styleModifier {
    property("-webkit-line-clamp", maxLines.toString())
    property("line-clamp", maxLines.toString())
    property("-webkit-box-orient", "vertical")
}

inline fun Float.take255() = this * 255