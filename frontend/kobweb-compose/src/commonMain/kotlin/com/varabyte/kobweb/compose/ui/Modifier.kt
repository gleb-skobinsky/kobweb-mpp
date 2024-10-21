package com.varabyte.kobweb.compose.ui

import androidx.compose.runtime.*

@Suppress("ModifierFactoryExtensionFunction")
@Stable
expect interface Modifier {
    fun <R> foldIn(initial: R, operation: (R, ModifierElement) -> R): R
    fun <R> foldOut(initial: R, operation: (ModifierElement, R) -> R): R
    fun any(predicate: (ModifierElement) -> Boolean): Boolean
    fun all(predicate: (ModifierElement) -> Boolean): Boolean

    open infix fun then(other: Modifier): Modifier

    companion object : Modifier
}

expect interface ModifierElement
