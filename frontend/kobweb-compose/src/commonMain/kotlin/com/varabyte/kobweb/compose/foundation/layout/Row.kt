package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier

expect interface RowScope

@Composable
expect inline fun Row(
    modifier: Modifier,
    crossinline content: @Composable RowScope.() -> Unit
)