package com.amerharb.shape

import com.amerharb.shape.models.TemperatureUnit
import com.amerharb.shape.service.DataProvider
import io.ktor.application.*
import io.ktor.client.features.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        gson {
        }
    }
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title { +"SHAPE | Weather API Assigment" }
                }
                body {
                    h1 { +"SHAPE" }
                    h2 { +"Weather API Assigment" }
                    h3 { +"Web API calls:" }
                    p { +"http://weather.amerharb.com/weather/locations/{Location Name}" }
                    p { +"http://weather.amerharb.com/weather/summary?unit={celsius|fahrenheit}&locations={Location Name}" }
                    p { }
                    p { }
                    h3 { +"Statistics:" }
                    p { +"call to OpenWeatherMap: ${DataProvider.getCountCallToApi()}" }
                    p { +"call from Cache: ${DataProvider.getCountCallFromCache()}" }
                }
            }
        }

        route("/weather") {
            get("/summary") {
                val tempUnit = when (call.request.queryParameters["unit"]) {
                    "celsius" -> TemperatureUnit.Celsius
                    "fahrenheit" -> TemperatureUnit.Fahrenheit
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "unit variable must be 'celsius' or 'fahrenheit'")
                        return@get
                    }
                }
                val locations = call.request.queryParameters["locations"]?.split(",")
                if (locations == null) {
                    call.respond(HttpStatusCode.BadRequest, "locations is not provided")
                    return@get
                }
                println("unit: $tempUnit")
                println("locations: $locations")

                try {
                    call.respond(
                        HttpStatusCode.OK,
                        DataProvider.getLocationsTemp(locations, tempUnit),
                    )
                } catch (e: ClientRequestException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/locations/{locationId}") {
                val locationId = call.parameters["locationId"]
                if (locationId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "location is not valid")
                    return@get
                }
                println("locationsId: $locationId")

                try {
                    call.respond(
                        HttpStatusCode.OK,
                        DataProvider.getLocationsForecast(locationId),
                    )
                } catch (e: ClientRequestException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
