package com.example.demobank.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onCardAdded: (cardHolder: String, cardType: String, color: String, balance: Double) -> Unit
) {
    var cardHolder by remember { mutableStateOf("John Doe") }
    var cardType by remember { mutableStateOf("VISA") }
    var cardTypeExpanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf("blue") }
    var colorExpanded by remember { mutableStateOf(false) }
    var initialBalance by remember { mutableStateOf("1000.00") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Добавить карту",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                }

                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("Держатель карты") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Card Type
                ExposedDropdownMenuBox(
                    expanded = cardTypeExpanded,
                    onExpandedChange = { cardTypeExpanded = !cardTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = cardType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Тип карты") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cardTypeExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = cardTypeExpanded,
                        onDismissRequest = { cardTypeExpanded = false }
                    ) {
                        listOf("VISA", "MasterCard", "Мир").forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    cardType = type
                                    cardTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Card Color
                ExposedDropdownMenuBox(
                    expanded = colorExpanded,
                    onExpandedChange = { colorExpanded = !colorExpanded }
                ) {
                    OutlinedTextField(
                        value = when (selectedColor) {
                            "blue" -> "Синяя"
                            "purple" -> "Фиолетовая"
                            "green" -> "Зелёная"
                            "orange" -> "Оранжевая"
                            else -> selectedColor
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Цвет карты") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = colorExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = colorExpanded,
                        onDismissRequest = { colorExpanded = false }
                    ) {
                        mapOf(
                            "blue" to "Синяя",
                            "purple" to "Фиолетовая",
                            "green" to "Зелёная",
                            "orange" to "Оранжевая"
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedColor = value
                                    colorExpanded = false
                                }
                            )
                        }
                    }
                }

                // Initial Balance
                OutlinedTextField(
                    value = initialBalance,
                    onValueChange = { initialBalance = it },
                    label = { Text("Начальный баланс") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            val balance = initialBalance.toDoubleOrNull() ?: 0.0
                            if (cardHolder.isNotEmpty() && balance >= 0) {
                                onCardAdded(cardHolder, cardType, selectedColor, balance)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = cardHolder.isNotEmpty() && initialBalance.toDoubleOrNull() != null
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}
