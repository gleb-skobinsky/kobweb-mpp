package com.varabyte.kobweb.material3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.material3.text.textStyleModifier
import org.jetbrains.compose.web.dom.Span
import com.varabyte.kobweb.compose.css.FontStyle as DomFontStyle
import org.jetbrains.compose.web.dom.Text as DomText

@Composable
fun Text(
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
    val actualStyle = style.merge(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign ?: TextAlign.Unspecified,
        lineHeight = lineHeight,
        fontFamily = fontFamily,
        textDecoration = textDecoration,
        fontStyle = fontStyle,
        letterSpacing = letterSpacing
    )
    Span(
        attrs = modifier
            .textStyleModifier(actualStyle, overflow)
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
fun FontStyle.toCssFontStyle() = when (this) {
    FontStyle.Normal -> DomFontStyle.Normal
    FontStyle.Italic -> DomFontStyle.Italic
    else -> DomFontStyle.Normal
}