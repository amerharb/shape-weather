package com.amerharb.shape

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
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
            handleRequest(HttpMethod.Get, "/weather/summary?unit=celsius&locations=cph,mmx").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("summary", response.content)
            }
            handleRequest(HttpMethod.Get, "/weather/summary?unit=fahrenheit&locations=cph,mmx").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("summary", response.content)
            }
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
            handleRequest(HttpMethod.Get, "/weather/locations/5").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("location:5", response.content)
            }
            handleRequest(HttpMethod.Get, "/weather/locations/").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}
