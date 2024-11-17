package com.varabyte.kobweb.material3.text

import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.isSpecified
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.applyIf
import com.varabyte.kobweb.compose.ui.applyNullable
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toCssColor
import com.varabyte.kobweb.material3.cssName
import com.varabyte.kobweb.material3.toCssFontStyle
import org.jetbrains.compose.web.css.px

internal fun Modifier.textStyleModifier(
    style: TextStyle
): Modifier {
    return with(style) {
        this@textStyleModifier
            .applyIf(fontSize.isSpecified) { fontSize(fontSize.value.px) }
            .applyIf(color.isSpecified) { color(color.toCssColor()) }
            .applyNullable(fontStyle) { fontStyle(it.toCssFontStyle()) }
            .applyNullable(fontWeight) { fontWeight(it.weight) }
            .applyNullable(fontFamily) { fontFamily(it.cssName) }
            .applyIf(textAlign != TextAlign.Unspecified) { textAlign(textAlign.toKobweb()) }
            .applyIf(lineHeight.isSpecified) { lineHeight(lineHeight.value.px) }
            .applyNullable(textDecoration) { Modifier.textDecorationLine(it.toKobweb()) }
            .applyIf(letterSpacing.isSpecified) { letterSpacing(letterSpacing.value.px) }
            .applyNullable(shadow) { if (shadow != Shadow.None) textShadow(it) else this }
            .applyNullable(fontSynthesis) { cssFontSynthesis(it) }
    }
}

