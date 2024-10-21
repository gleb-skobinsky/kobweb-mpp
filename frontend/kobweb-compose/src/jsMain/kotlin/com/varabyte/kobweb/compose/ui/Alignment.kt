@file:Suppress("ConvertObjectToDataObject") // Don't create data objects, no need to generate extra code

package com.varabyte.kobweb.compose.ui

import androidx.compose.ui.Alignment as JbAlignment

sealed interface Alignment {
    sealed interface Vertical
    sealed interface Horizontal

    object TopStart : Alignment
    object TopCenter : Alignment
    object TopEnd : Alignment
    object CenterStart : Alignment
    object Center : Alignment
    object CenterEnd : Alignment
    object BottomStart : Alignment
    object BottomCenter : Alignment
    object BottomEnd : Alignment

    object Top : Vertical
    object CenterVertically : Vertical
    object Bottom : Vertical

    object Start : Horizontal
    object CenterHorizontally : Horizontal
    object End : Horizontal
}

fun JbAlignment.toKobWeb() = when (this) {
    JbAlignment.TopStart -> Alignment.TopStart
    JbAlignment.TopCenter -> Alignment.TopCenter
    JbAlignment.TopEnd -> Alignment.TopEnd
    JbAlignment.CenterStart -> Alignment.CenterStart
    JbAlignment.Center -> Alignment.Center
    JbAlignment.CenterEnd -> Alignment.CenterEnd
    JbAlignment.BottomStart -> Alignment.BottomStart
    JbAlignment.BottomEnd -> Alignment.BottomEnd
    JbAlignment.BottomCenter -> Alignment.BottomCenter
    else -> Alignment.TopStart
}

fun JbAlignment.Horizontal.toKobWeb() = when (this) {
    JbAlignment.Start -> Alignment.Start
    JbAlignment.CenterHorizontally -> Alignment.CenterHorizontally
    JbAlignment.End -> Alignment.End
    else -> Alignment.Start
}

fun JbAlignment.Vertical.toKobWeb() = when (this) {
    JbAlignment.Top -> Alignment.Top
    JbAlignment.CenterVertically -> Alignment.CenterVertically
    JbAlignment.Bottom -> Alignment.Bottom
    else -> Alignment.Top
}
