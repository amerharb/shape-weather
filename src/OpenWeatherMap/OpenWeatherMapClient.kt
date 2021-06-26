package com.amerharb.shape.OpenWeatherMap

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

    suspend fun getLocationTemp(location: String): String {
        val response: HttpResponse = client.get("$host$baseRoute/weather") {
            headers {
            }
            parameter("q", location)
            parameter("appid", apiKey)
        }
        return response.receive()
    }
}
