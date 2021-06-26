package com.amerharb.shape

import com.amerharb.shape.openweathermap.OpenWeatherMapClient
import com.google.gson.Gson
import kotlin.test.Test
import kotlinx.coroutines.*


class OpenWeatherMapClientTest {
    var gson = Gson()

    @Test
    fun getLocationTemp() {
        runBlocking {
            launch { println(OpenWeatherMapClient().getLocationTemp("Malmo")) }
        }
//        withTestApplication({ module(testing = true) }) {
//            handleRequest(HttpMethod.Get, "/").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals("Weather API Assignment for SHAPE", response.content)
//            }
//        }
    }

}
