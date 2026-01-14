package com.example.demobank.ui.screens.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.demobank.ui.components.ContactPickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: TransferViewModel = viewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val transferStatus by viewModel.transferStatus.collectAsState()

    var transferMode by remember { mutableStateOf<TransferMode>(TransferMode.BetweenMyCards) }
    var fromCardExpanded by remember { mutableStateOf(false) }
    var toCardExpanded by remember { mutableStateOf(false) }
    var selectedFromCard by remember { mutableStateOf<Long?>(null) }
    var selectedToCard by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    var recipientPhone by remember { mutableStateOf("") }
    var showContactPicker by remember { mutableStateOf(false) }

    // Получаем выбранную карту для отображения баланса
    val fromCard = cards.find { it.id == selectedFromCard }

    LaunchedEffect(transferStatus) {
        if (transferStatus is TransferStatus.Success) {
            amount = ""
            selectedFromCard = null
            selectedToCard = null
            recipientPhone = ""
            viewModel.resetStatus()
        }
    }

    if (showContactPicker) {
        ContactPickerDialog(
            onDismiss = { showContactPicker = false },
            onContactSelected = { contact ->
                recipientPhone = contact.phoneNumber
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Перевод средств", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transfer Mode Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Тип перевода",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = transferMode is TransferMode.BetweenMyCards,
                            onClick = {
                                transferMode = TransferMode.BetweenMyCards
                                recipientPhone = ""
                            },
                            label = { Text("Между своими счетами") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = transferMode is TransferMode.ToOtherPerson,
                            onClick = {
                                transferMode = TransferMode.ToOtherPerson
                                selectedToCard = null
                            },
                            label = { Text("По номеру") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ContactPage,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // From Card (always shown)
            ExposedDropdownMenuBox(
                expanded = fromCardExpanded,
                onExpandedChange = { fromCardExpanded = !fromCardExpanded }
            ) {
                OutlinedTextField(
                    value = cards.find { it.id == selectedFromCard }?.let {
                        "${it.cardType} •••• ${it.cardNumber.takeLast(4)}"
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("С карты") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromCardExpanded) }
                )

                ExposedDropdownMenu(
                    expanded = fromCardExpanded,
                    onDismissRequest = { fromCardExpanded = false }
                ) {
                    cards.forEach { card ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("${card.cardType} •••• ${card.cardNumber.takeLast(4)}")
                                    Text(
                                        "Баланс: $${"%.2f".format(card.balance)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            },
                            onClick = {
                                selectedFromCard = card.id
                                fromCardExpanded = false
                            }
                        )
                    }
                }
            }

            // Показываем баланс выбранной карты
            if (fromCard != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Доступно:",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "$${"%.2f".format(fromCard.balance)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Conditional UI based on transfer mode
            when (transferMode) {
                TransferMode.BetweenMyCards -> {
                    // To Card dropdown
                    ExposedDropdownMenuBox(
                        expanded = toCardExpanded,
                        onExpandedChange = { toCardExpanded = !toCardExpanded }
                    ) {
                        OutlinedTextField(
                            value = cards.find { it.id == selectedToCard }?.let {
                                "${it.cardType} •••• ${it.cardNumber.takeLast(4)}"
                            } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("На карту") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toCardExpanded) }
                        )

                        ExposedDropdownMenu(
                            expanded = toCardExpanded,
                            onDismissRequest = { toCardExpanded = false }
                        ) {
                            cards.forEach { card ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text("${card.cardType} •••• ${card.cardNumber.takeLast(4)}")
                                            Text(
                                                "Баланс: $${"%.2f".format(card.balance)}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedToCard = card.id
                                        toCardExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                TransferMode.ToOtherPerson -> {
                    // Phone number input with contact picker
                    OutlinedTextField(
                        value = recipientPhone,
                        onValueChange = { recipientPhone = it },
                        label = { Text("Номер телефона") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("+7 (999) 123-45-67") },
                        trailingIcon = {
                            IconButton(onClick = { showContactPicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.ContactPage,
                                    contentDescription = "Выбрать контакт"
                                )
                            }
                        }
                    )
                }
            }

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сумма") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )

            // Transfer Button
            Button(
                onClick = {
                    val fromId = selectedFromCard
                    val amountValue = amount.toDoubleOrNull()

                    if (fromId != null && amountValue != null && amountValue > 0) {
                        when (transferMode) {
                            TransferMode.BetweenMyCards -> {
                                val toId = selectedToCard
                                if (toId != null) {
                                    viewModel.performTransfer(fromId, toId, amountValue)
                                }
                            }
                            TransferMode.ToOtherPerson -> {
                                if (recipientPhone.isNotEmpty()) {
                                    viewModel.performExternalTransfer(fromId, amountValue, recipientPhone)
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = when (transferMode) {
                    TransferMode.BetweenMyCards ->
                        selectedFromCard != null &&
                                selectedToCard != null &&
                                amount.toDoubleOrNull() != null &&
                                amount.toDoubleOrNull()!! > 0 &&
                                transferStatus !is TransferStatus.Loading
                    TransferMode.ToOtherPerson ->
                        selectedFromCard != null &&
                                recipientPhone.isNotEmpty() &&
                                amount.toDoubleOrNull() != null &&
                                amount.toDoubleOrNull()!! > 0 &&
                                transferStatus !is TransferStatus.Loading
                }
            ) {
                if (transferStatus is TransferStatus.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Перевести")
                }
            }

            // Status Messages
            when (val status = transferStatus) {
                is TransferStatus.Success -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "Перевод выполнен успешно!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                is TransferStatus.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Ошибка: ${status.message}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

sealed class TransferMode {
    object BetweenMyCards : TransferMode()
    object ToOtherPerson : TransferMode()
}
