package com.amerharb.shape

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {
    var gson = Gson()

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testSummary() {
        withTestApplication({ module(testing = true) }) {
            //todo: uncomment these where we have testing true in place from dataprovider
            handleRequest(HttpMethod.Get, "/weather/summary?unit=celsius&locations=malmo").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
            handleRequest(HttpMethod.Get, "/weather/summary?unit=celsius&locations=invalidCity").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
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
    fun testLocationsCaching() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/weather/locations/invalidCity").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            var startTime = System.currentTimeMillis()
            handleRequest(HttpMethod.Get, "/weather/locations/malmo").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
            val t1 = System.currentTimeMillis() - startTime

            startTime = System.currentTimeMillis()
            handleRequest(HttpMethod.Get, "/weather/locations/malmo").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
            val t2 = System.currentTimeMillis() - startTime

            println("first call time: $t1")
            println("second call time: $t2")
            assertTrue("second call faster than first") { t2 < t1 }
            assertTrue("second call less than 20 ms") { t2 < 10 }
        }
    }

    @Test
    fun testLocations() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/weather/locations/").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            handleRequest(HttpMethod.Get, "/weather/locations/  ").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}
