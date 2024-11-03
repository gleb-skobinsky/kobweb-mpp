package com.varabyte.kobweb.material3.text

import androidx.compose.ui.text.style.TextAlign
import com.varabyte.kobweb.compose.css.TextAlign as KobWebAlign


internal fun TextAlign.toKobweb(): KobWebAlign = when (this) {
    TextAlign.Left -> KobWebAlign.Left
    TextAlign.Right -> KobWebAlign.Right
    TextAlign.Center -> KobWebAlign.Center
    TextAlign.Justify -> KobWebAlign.Justify
    TextAlign.Start -> KobWebAlign.Start
    TextAlign.End -> KobWebAlign.End
    else -> KobWebAlign.Start
}