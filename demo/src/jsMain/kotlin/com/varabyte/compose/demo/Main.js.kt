package com.varabyte.compose.demo

import com.varabyte.kobweb.compose.KobwebComposeStyles
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        KobwebComposeStyles()
        App()
    }
}