package com.varabyte.kobweb.material3.text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.applyNullable
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toCssColor
import com.varabyte.kobweb.material3.cssName
import com.varabyte.kobweb.material3.toCssFontStyle
import org.jetbrains.compose.web.css.*

internal fun Modifier.textStyleModifier(
    style: TextStyle
): Modifier {
    return with(style) {
        this@textStyleModifier
            .thenIf(fontSize != TextUnit.Unspecified) { Modifier.fontSize(fontSize.value.px) }
            .thenIf(color != Color.Unspecified) { Modifier.color(color.toCssColor()) }
            .applyNullable(fontStyle?.toCssFontStyle()) { fontStyle(it) }
            .applyNullable(fontWeight) { fontWeight(it.weight) }
            .applyNullable(fontFamily) { fontFamily(it.cssName) }
            .thenIf(textAlign != TextAlign.Unspecified) { Modifier.textAlign(textAlign.toKobweb()) }
            .thenIf(lineHeight != TextUnit.Unspecified) { Modifier.lineHeight(lineHeight.value.px) }
            .applyNullable(textDecoration) { Modifier.textDecorationLine(it.toKobweb()) }
    }
}