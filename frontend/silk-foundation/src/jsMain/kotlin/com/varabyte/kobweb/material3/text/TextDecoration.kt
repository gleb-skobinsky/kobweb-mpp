package com.varabyte.kobweb.material3.text

import androidx.compose.ui.text.style.TextDecoration
import com.varabyte.kobweb.compose.css.*

internal fun TextDecoration.toKobweb(): TextDecorationLine = when (this) {
    TextDecoration.None -> TextDecorationLine.None
    TextDecoration.Underline -> TextDecorationLine.Underline
    TextDecoration.LineThrough -> TextDecorationLine.LineThrough
    TextDecoration.Underline + TextDecoration.LineThrough -> TextDecorationLine.Underline + TextDecorationLine.LineThrough
    else -> TextDecorationLine.None
}