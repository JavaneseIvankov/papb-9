package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.theme.Praktikum8_CoroutinesFlow_245150201111011PriyambodoArundayaSatmikaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Praktikum8_CoroutinesFlow_245150201111011PriyambodoArundayaSatmikaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavigationLabApp()
                    }
                }
            }
        }
    }
}
