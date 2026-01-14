package com.example.demobank.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Главная")
    object Transactions : Screen("История")
    object Transfer : Screen("Перевод")
    object Profile : Screen("Профиль")
    object CardsManagement : Screen("cards_management")
}