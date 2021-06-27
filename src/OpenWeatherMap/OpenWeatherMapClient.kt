package com.amerharb.shape.openweathermap

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class OpenWeatherMapClient {
    private val apiKey = "f8ba068220a0879ecef5fa473a539a1f"
    private val host = "http://api.openweathermap.org"
    private val baseRoute = "/data/2.5"
    private val client = HttpClient(Apache)

    suspend fun getLocationTemp(location: String): WeatherResponse {
        val response = getClient("weather", location)
        val body: String = response.receive()
        val gson = Gson()
        return gson.fromJson(body, WeatherResponse::class.java)
    }

    suspend fun getForecast(location: String): ForecastResponse {
        val response = getClient("forecast", location)
        val body: String = response.receive()
        val gson = Gson()
        return gson.fromJson(body, ForecastResponse::class.java)
    }

    private suspend fun getClient(route:String, location: String):HttpResponse{
        return  client.get("$host$baseRoute/$route") {
            headers {
            }
            parameter("q", location)
            parameter("appid", apiKey)
        }
    }
}
