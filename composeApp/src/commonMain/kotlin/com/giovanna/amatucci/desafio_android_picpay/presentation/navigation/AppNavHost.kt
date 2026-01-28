package com.giovanna.amatucci.desafio_android_picpay.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.giovanna.amatucci.desafio_android_picpay.presentation.feature.ContactsListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController, startDestination = ContactsListScreen
    ) {
        composable<ContactsListScreen> {
            ContactsListScreen()
        }
    }
}