package com.giovanna.amatucci.desafio_android_picpay

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.giovanna.amatucci.desafio_android_picpay.presentation.navigation.AppNavHost
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.PicpayTheme

@Composable
fun App() {
    PicpayTheme {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
    }
}