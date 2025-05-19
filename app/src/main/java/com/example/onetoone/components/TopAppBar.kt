package com.example.onetoone.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.onetoone.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar(topBarName: String){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = topBarName)}, colors = TopAppBarDefaults.topAppBarColors(containerColor = Yellow))
        }
    ) {
        Box(modifier = Modifier.padding(it)) {

        }
    }
}