package com.varabyte.compose.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() {
    System.setProperty("apple.awt.application.appearance", "system")
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(placement = WindowPlacement.Maximized),
            title = "ComposeBricks",
        ) {
            App()
        }
    }
}