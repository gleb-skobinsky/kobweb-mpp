package com.varabyte.compose.demo

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.background
import com.varabyte.kobweb.compose.ui.fillMaxSize

@Composable
fun App() {
    Box(Modifier.fillMaxSize().background(Color.Cyan)) {}
}