package com.example.demobank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BankCard(
    cardNumber: String,
    cardHolder: String,
    balance: Double,
    cardType: String,
    color: String,
    modifier: Modifier = Modifier
) {
    val gradient = when (color) {
        "blue" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF2196F3), Color(0xFF1976D2))
        )
        "purple" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
        )
        "green" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))
        )
        "orange" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFF9800), Color(0xFFF57C00))
        )
        else -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF607D8B), Color(0xFF455A64))
        )
    }


    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Card Type
                Text(
                    text = cardType,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Column {
                    // Card Number
                    Text(
                        text = formatCardNumber(cardNumber),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Держатель карты",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = cardHolder,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Баланс",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "$${"%.2f".format(balance)}",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatCardNumber(number: String): String {
    return number.chunked(4).joinToString(" ")
}
