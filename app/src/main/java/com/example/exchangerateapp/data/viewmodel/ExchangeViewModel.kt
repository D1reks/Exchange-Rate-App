package com.example.exchangerateapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangerateapp.retrofit.ExchangeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExchangeViewModel @Inject constructor(
private val apiService: ExchangeApiService
) : ViewModel() {
    private val _rates = MutableStateFlow<Map<String, Double>?>(null)
    val rates: StateFlow<Map<String, Double>?> = _rates

    init {
        fetchRates()
    }

    fun fetchRates() {
        viewModelScope.launch {
            try {
                val response = apiService.getLatestRates()
                _rates.value = response.currencyRates
            } catch (e: Exception) {
                println(e.message)
                _rates.value = null
            }
        }
    }

}