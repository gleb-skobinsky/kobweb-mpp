package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier

actual typealias RowScope = RowScope

@Composable
actual inline fun Row(
    modifier: Modifier,
    crossinline content: @Composable RowScope.() -> Unit
) = androidx.compose.foundation.layout.Row(
    modifier = modifier,
    content = content
)