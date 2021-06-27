package com.amerharb.shape

import com.amerharb.shape.openweathermap.OpenWeatherMapClient
import com.google.gson.Gson
import kotlin.test.Test
import kotlinx.coroutines.*


class OpenWeatherMapClientTest {
    @Test //Note: this is not unit test, it is just used for debugging
    fun getLocationTemp() {
        runBlocking {
            launch { println(OpenWeatherMapClient().getLocationTemp("Malmo")) }
        }
    }

    @Test //Note: this is not unit test, it is just used for debugging
    fun getForecastTemp() {
        runBlocking {
            launch { println(OpenWeatherMapClient().getForecast("Malmo")) }
        }
    }
}
