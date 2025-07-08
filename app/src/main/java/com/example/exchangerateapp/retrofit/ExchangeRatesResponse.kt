package com.example.exchangerateapp.retrofit

data class ExchangeRatesResponse(
    val baseCurrency: String,
    val ratesDate: String,
    val currencyRates: Map<String, Double>
)
