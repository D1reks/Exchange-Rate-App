package com.example.exchangerateapp.retrofit

import retrofit2.http.GET

interface ExchangeApiService {
    @GET("latest/USD")
    suspend fun getLatestRates(): ExchangeRatesResponse
}