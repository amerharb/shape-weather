package com.amerharb.shape

import com.amerharb.shape.models.Location
import com.amerharb.shape.models.LocationTemp
import com.amerharb.shape.models.Summary
import com.amerharb.shape.models.TemperatureUnit
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    var gson = Gson()

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Weather API Assignment for SHAPE", response.content)
            }
        }
    }

    @Test
    fun testSummary() {
        withTestApplication({ module(testing = true) }) {
            //todo: uncomment these where we have testing true in place from dataprovider
//            handleRequest(HttpMethod.Get, "/weather/summary?unit=celsius&locations=cph,mmx").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals(
//                    gson.toJson(
//                        Summary(
//                            tempUnit = TemperatureUnit.Celsius,
//                            locations = listOf(
//                                LocationTemp("cph", 10F),
//                                LocationTemp("mmx", 10F),
//                            ),
//                        )
//                    ),
//                    response.content,
//                )
//            }
//            handleRequest(HttpMethod.Get, "/weather/summary?unit=fahrenheit&locations=cph,sto").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals(
//                    gson.toJson(
//                        Summary(
//                            tempUnit = TemperatureUnit.Fahrenheit,
//                            locations = listOf(
//                                LocationTemp("cph", 10F),
//                                LocationTemp("sto", 10F),
//                            ),
//                        )
//                    ),
//                    response.content,
//                )            }
            handleRequest(HttpMethod.Get, "/weather/summary?").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
            handleRequest(HttpMethod.Get, "/weather/summary?unit=xx&locations=cph,mmx").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
            handleRequest(HttpMethod.Get, "/weather/summary?locations=cph,mmx").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
            handleRequest(HttpMethod.Get, "/weather/summary?nit=celsius").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun testLocations() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/weather/locations/cph").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    gson.toJson(
                        Location(
                            location = "cph",
                            tempUnit = TemperatureUnit.Celsius,
                            listOf(20, 18, 23, 30, 25)
                        )
                    ),
                    response.content,
                )
            }
            handleRequest(HttpMethod.Get, "/weather/locations/").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            handleRequest(HttpMethod.Get, "/weather/locations/  ").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}
