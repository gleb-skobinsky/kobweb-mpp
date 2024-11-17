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
import androidx.compose.ui.unit.isSpecified
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.WordBreak
import com.varabyte.kobweb.compose.ui.JsModifier
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.applyIf
import com.varabyte.kobweb.compose.ui.applyNullable
import com.varabyte.kobweb.compose.ui.lineClamp
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.wordBreak
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.material3.text.textStyleModifier
import com.varabyte.kobweb.material3.text.toKobweb
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
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
            .textStyleModifier(actualStyle)
            .applyNullable(overflow.toKobweb()) { textOverflow(it) }
            .softWrap(softWrap)
            .applyIf(maxLines != Int.MAX_VALUE) {
                overflow(Overflow.Hidden)
                    .display(DisplayStyle("-webkit-box"))
                    .lineClamp(maxLines)
            }
            .applyIf(minLines != 1) {
                if (actualStyle.lineHeight.isSpecified) {
                    minHeight(actualStyle.lineHeight.times(minLines).value.px)
                } else {
                    minHeight(minLines.em)
                }
            }
            .toAttrs()
    ) {
        DomText(text)
    }
}

private fun JsModifier.softWrap(softWrap: Boolean) = then(
    if (softWrap) {
        Modifier
            .wordBreak(WordBreak.Normal)
    } else {
        Modifier
            .whiteSpace(WhiteSpace.NoWrap)
            .overflow(Overflow.Hidden)
    }
)

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