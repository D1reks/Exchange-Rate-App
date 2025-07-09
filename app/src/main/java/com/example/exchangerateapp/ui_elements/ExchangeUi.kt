package com.example.exchangerateapp.ui_elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exchangerateapp.data.viewmodel.ExchangeViewModel


class ExchangeUi {

    @Composable
    fun ExchangeApp(viewModel: ExchangeViewModel = viewModel()) {
        val ratesState by viewModel.rates.collectAsState()

        var leftCurrency by remember { mutableStateOf("USD") }
        var rightCurrency by remember { mutableStateOf("EUR") }

        var leftAmount by remember { mutableStateOf("0") }
        var rightAmount by remember { mutableStateOf("0") }

        val currencies = ratesState?.keys?.sorted() ?: listOf("USD", "EUR", "GBP", "JPY", "AUD")

        var currentRate by remember { mutableStateOf(1.0) }

        LaunchedEffect(leftCurrency, rightCurrency, ratesState) {
            if (ratesState != null && ratesState!!.containsKey(leftCurrency) && ratesState!!.containsKey(rightCurrency)) {
                val rateLeft = ratesState!![leftCurrency] ?: 1.0
                val rateRight = ratesState!![rightCurrency] ?: 1.0
                currentRate = rateRight / rateLeft
            }
        }

        fun updateRightAmount() {
            val amount = leftAmount.toDoubleOrNull() ?: 0.0
            val result = amount * currentRate
            rightAmount = String.format("%.4f", result)
        }

        fun updateLeftAmount() {
            val amount = rightAmount.toDoubleOrNull() ?: 0.0
            val result = amount / currentRate
            leftAmount = String.format("%.4f", result)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CurrencyConverterRow(
                ratesState = ratesState,
                leftCurrency = leftCurrency,
                onLeftCurrencyChange = { leftCurrency = it },
                rightCurrency = rightCurrency,
                onRightCurrencyChange = { rightCurrency = it },
                leftAmount = leftAmount,
                onLeftAmountChange = {
                    leftAmount = it
                    updateRightAmount()
                },
                rightAmount = rightAmount,
                onRightAmountChange = {
                    rightAmount = it
                    updateLeftAmount()
                },
                currentRate = currentRate,
                currencies = currencies
            )
        }

    }
    @Composable
    fun CurrencyConverterRow(
        ratesState: Map<String, Double>?,
        leftCurrency: String,
        onLeftCurrencyChange: (String) -> Unit,
        rightCurrency: String,
        onRightCurrencyChange: (String) -> Unit,
        leftAmount: String,
        onLeftAmountChange: (String) -> Unit,
        rightAmount: String,
        onRightAmountChange: (String) -> Unit,
        currentRate: Double,
        currencies: List<String>
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DropdownMenuBox(
                    options = currencies,
                    selectedOption = leftCurrency,
                    onOptionSelected = onLeftCurrencyChange
                )
                OutlinedTextField(
                    value = leftAmount,
                    onValueChange = onLeftAmountChange,
                    label = { Text("Количество") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            // Курс
            Text(
                text = if (ratesState != null && ratesState.containsKey(leftCurrency) && ratesState.containsKey(rightCurrency))
                    "1 $leftCurrency ≈ ${String.format("%.4f", currentRate)} $rightCurrency"
                else
                    "="
            )

            // Правая часть
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DropdownMenuBox(
                    options = currencies,
                    selectedOption = rightCurrency,
                    onOptionSelected = onRightCurrencyChange
                )
                OutlinedTextField(
                    value = rightAmount,
                    onValueChange = onRightAmountChange,
                    label = { Text("Количество") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
    @Composable
    fun DropdownMenuBox(
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedOption)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}