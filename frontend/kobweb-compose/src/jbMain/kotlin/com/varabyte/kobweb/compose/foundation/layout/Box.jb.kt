package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier

actual typealias BoxScope = androidx.compose.foundation.layout.BoxScope

@Composable
actual inline fun Box(
    modifier: Modifier,
    contentAlignment: Alignment,
    propagateMinConstraints: Boolean,
    content: @Composable BoxScope.() -> Unit
) = androidx.compose.foundation.layout.Box(
    modifier = modifier,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = content
)