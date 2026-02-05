package com.giovanna.amatucci.desafio_android_picpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.giovanna.amatucci.desafio_android_picpay.presentation.navigation.AppNavHost
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.PicpayTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PicpayTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}