package com.varabyte.kobweb.material3.text

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toCssColor
import org.jetbrains.compose.web.css.px
import com.varabyte.kobweb.compose.css.TextOverflow as KobTextOverflow

@Stable
internal fun TextAlign.toKobweb(): com.varabyte.kobweb.compose.css.TextAlign = when (this) {
    TextAlign.Left -> com.varabyte.kobweb.compose.css.TextAlign.Left
    TextAlign.Right -> com.varabyte.kobweb.compose.css.TextAlign.Right
    TextAlign.Center -> com.varabyte.kobweb.compose.css.TextAlign.Center
    TextAlign.Justify -> com.varabyte.kobweb.compose.css.TextAlign.Justify
    TextAlign.Start -> com.varabyte.kobweb.compose.css.TextAlign.Start
    TextAlign.End -> com.varabyte.kobweb.compose.css.TextAlign.End
    else -> com.varabyte.kobweb.compose.css.TextAlign.Start
}

@Stable
internal fun TextDecoration.toKobweb(): TextDecorationLine = when (this) {
    TextDecoration.None -> TextDecorationLine.None
    TextDecoration.Underline -> TextDecorationLine.Underline
    TextDecoration.LineThrough -> TextDecorationLine.LineThrough
    TextDecoration.Underline + TextDecoration.LineThrough -> TextDecorationLine.Underline + TextDecorationLine.LineThrough
    else -> TextDecorationLine.None
}

@Stable
internal fun Modifier.textShadow(shadow: Shadow): Modifier = this.textShadow(
    offsetX = shadow.offset.x.px,
    offsetY = shadow.offset.y.px,
    blurRadius = shadow.blurRadius.px,
    color = shadow.color.toCssColor()
)

@Stable
internal fun Modifier.cssFontSynthesis(value: FontSynthesis) = styleModifier {
    property("font-synthesis", value.toCssFontSynthesis())
}

@Stable
internal fun FontSynthesis.toCssFontSynthesis() = when (this) {
    FontSynthesis.None -> "none"
    FontSynthesis.Weight -> "weight"
    FontSynthesis.Style -> "style"
    FontSynthesis.All -> "weight style"
    else -> "none"
}

@Stable
internal fun TextOverflow.toKobweb(): KobTextOverflow? = when (this) {
    TextOverflow.Clip -> KobTextOverflow.Clip
    TextOverflow.Ellipsis -> KobTextOverflow.Ellipsis
    else -> null
}