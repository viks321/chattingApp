package com.example.onetoone.lodingScreen



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.LodingColor
import com.example.onetoone.ui.theme.Yellow


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewScreen(){
    lodingScreen()
}

@Composable
fun lodingScreen() {
    Box(modifier = Modifier.fillMaxSize().background(color = LodingColor).clickable(indication = null,              // 🔸 Disable ripple
        interactionSource = remember { MutableInteractionSource() }) {  }) {
        CircularProgressIndicator(
            color = Yellow,
            strokeWidth = 6.dp,
            modifier = Modifier
                .size(60.dp)
                .align(alignment = Alignment.Center)
        )
    }
}
