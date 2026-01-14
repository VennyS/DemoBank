package com.example.demobank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.demobank.ui.screens.cards.CardsManagementScreen
import com.example.demobank.ui.screens.home.HomeScreen
import com.example.demobank.ui.screens.profile.ProfileScreen
import com.example.demobank.ui.screens.transactions.TransactionsScreen
import com.example.demobank.ui.screens.transfer.TransferScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Transactions.route) {
            TransactionsScreen(navController = navController)
        }
        composable(Screen.Transfer.route) {
            TransferScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.CardsManagement.route) {
            CardsManagementScreen(navController = navController)
        }
    }
}
