package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.Div

/**
 * An element which grows to consume all remaining space in a [JsRow] or [Column].
 */
@Composable
fun Spacer() {
    Div(attrs = { classes("kobweb-spacer") })
}
