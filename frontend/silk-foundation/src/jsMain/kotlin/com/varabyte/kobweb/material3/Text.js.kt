package com.varabyte.kobweb.material3

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FileBasedFontFamily
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.font.LoadedFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.compose.ui.toCssColor
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Span
import com.varabyte.kobweb.compose.css.FontStyle as DomFontStyle
import org.jetbrains.compose.web.dom.Text as DomText


@Composable
actual fun Text(
    text: String,
    modifier: Modifier,
    color: Color,
    fontSize: TextUnit,
    fontStyle: FontStyle?,
    fontWeight: FontWeight?,
    fontFamily: FontFamily?,
    letterSpacing: TextUnit,
    textDecoration: TextDecoration?,
    textAlign: TextAlign?,
    lineHeight: TextUnit,
    overflow: TextOverflow,
    softWrap: Boolean,
    maxLines: Int,
    minLines: Int,
    onTextLayout: ((TextLayoutResult) -> Unit)?,
    style: TextStyle
) {
    Span(
        attrs = modifier
            .thenIf(fontSize != TextUnit.Unspecified) { Modifier.fontSize(fontSize.value.px) }
            .thenIf(color != Color.Unspecified) { Modifier.color(color.toCssColor()) }
            .applyNullable(fontStyle?.toCssFontStyle()) { fontStyle(it) }
            .applyNullable(fontWeight) { fontWeight(it.weight) }
            .applyNullable(fontFamily) { fontFamily(it.cssName) }
            .toAttrs()
    ) {
        DomText(text)
    }
}

val FontFamily.cssName: String
    get() = when (this) {
        is GenericFontFamily -> name
        else -> toString()
    }

@Stable
inline fun <T> Modifier.applyNullable(
    value: T?,
    applier: Modifier.(T) -> Modifier
): Modifier = value?.let { this.applier(it) } ?: this


@Stable
fun FontStyle.toCssFontStyle() = when (this) {
    FontStyle.Normal -> DomFontStyle.Normal
    FontStyle.Italic -> DomFontStyle.Italic
    else -> DomFontStyle.Normal
}