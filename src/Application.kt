package com.amerharb.shape

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("Weather API Assignment for SHAPE", contentType = ContentType.Text.Plain)
        }

        route("/weather") {
            get("/summary") {
                val unit = when (call.request.queryParameters["unit"]) {
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
                println("unit: $unit")
                println("locations: $locations")

                //TODO: replace mock date
                call.respond("summary")
            }

            get("/locations/{locationId}") {
                val locationId = call.parameters["locationId"]
                if (locationId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "location is not valid")
                    return@get
                }
                println("locationsId: $locationId")

                //TODO: replace mock date
                call.respond(
                    Location(
                        location = locationId,
                        tempUnit = TemperatureUnit.Celsius,
                        arrayOf(20, 18, 23, 30, 25)
                    )
                )
            }
        }
    }
}
