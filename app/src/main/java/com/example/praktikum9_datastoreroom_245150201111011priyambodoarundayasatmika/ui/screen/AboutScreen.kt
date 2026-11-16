package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("About", style = MaterialTheme.typography.headlineMedium)
        Text("Aplikasi Praktikum Navigation Compose")
        Text("Nama: Priyambodo Arundaya Satmika")
        Text("NIM: 245150201111011")
        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
