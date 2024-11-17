package com.varabyte.compose.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.background
import com.varabyte.kobweb.compose.ui.fillMaxSize
import com.varabyte.kobweb.compose.ui.width
import com.varabyte.kobweb.material3.Text

@Composable
fun App() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ) {
        Row(Modifier.width(100.dp)) {
            Text(
                text = "Hello world from KobWeb",
                fontSize = 12.sp,
                color = Color.Red,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                minLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}