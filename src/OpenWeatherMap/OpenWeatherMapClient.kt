package com.amerharb.shape.OpenWeatherMap

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class OpenWeatherMapClient {
    private val client = HttpClient(Apache)

    suspend fun getLocationTemp(location:String): String {
        val response: HttpResponse = client.get("http://api.openweathermap.org/data/2.5/weather") {
            headers {
            }
            parameter("q", location)
            parameter("appid", "f8ba068220a0879ecef5fa473a539a1f")
        }
        val stringBody: String = response.receive()
        return stringBody
    }
}
