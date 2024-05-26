package com.jonava.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonava.realtimeweather.api.Constant
import com.jonava.realtimeweather.api.NetworkResponse
import com.jonava.realtimeweather.api.RetrofitInstance
import com.jonava.realtimeweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResulut = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResulut

    fun getData(city: String) {
        _weatherResulut.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeatherData(Constant.apiKey, city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResulut.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResulut.value = NetworkResponse.Error("Error al obtener los datos del clima")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "getData: ${e.message}")
                _weatherResulut.value = NetworkResponse.Error("Error al obtener los datos del clima")
            }
        }
    }
}