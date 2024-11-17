package com.varabyte.kobweb.compose.foundation.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier

actual typealias RowScope = JsRowScope

@Composable
actual inline fun Row(
    modifier: Modifier,
    crossinline content: @Composable RowScope.() -> Unit
) = JsRow(modifier, content = content)