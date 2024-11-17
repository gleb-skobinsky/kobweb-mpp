package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier

@LayoutScopeMarker
@Immutable
expect interface BoxScope {
    @Stable
    fun Modifier.align(alignment: Alignment): Modifier
}

@Composable
expect inline fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    crossinline content: @Composable BoxScope.() -> Unit
)

