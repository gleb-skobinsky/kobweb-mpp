package com.varabyte.kobweb.material3.style.selectors

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.material3.style.StyleScope

fun StyleScope.children(vararg elements: String, createModifier: () -> Modifier) =
    cssRule(" > :is(${elements.joinToString()})", createModifier)

fun StyleScope.descendants(vararg elements: String, createModifier: () -> Modifier) =
    cssRule(" :is(${elements.joinToString()})", createModifier)

fun StyleScope.nextSiblings(vararg elements: String, createModifier: () -> Modifier) =
    cssRule(" + :is(${elements.joinToString()})", createModifier)

fun StyleScope.subsequentSiblings(vararg elements: String, createModifier: () -> Modifier) =
    cssRule(" ~ :is(${elements.joinToString()})", createModifier)
