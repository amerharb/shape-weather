package com.amerharb.shape

import com.amerharb.shape.models.Location
import com.amerharb.shape.models.LocationTemp
import com.amerharb.shape.models.Summary
import com.amerharb.shape.models.TemperatureUnit
import com.amerharb.shape.openweathermap.ForecastResponse
import com.amerharb.shape.openweathermap.OpenWeatherMapClient
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

/**
 *  DataProvider is single instance object that
 *  1. take data from open weather map on demand
 *  2. cache it for next calls
 *  3. convert Kelvin to Cel or Feh
 */
object DataProvider {
    private val source = OpenWeatherMapClient()
    private val cacheTemp = mutableMapOf<String, Float>()
    private val cacheForecast = mutableMapOf<String, LocationForecast>()
    private val ttlForecast = 1000L * 60L * 60L * 5L // 5 hours

    fun getLocationsTemp(locations: List<String>, tempUnit: TemperatureUnit): Summary =
        runBlocking {
            val callList = locations.map { it to async { getCacheOrCallTemp(it, tempUnit) } }
            val locationTemps = callList.map { LocationTemp(it.first, it.second.await()) }
            Summary(tempUnit, locationTemps)
        }

    fun getLocationsForecast(location: String, tempUnit: TemperatureUnit = TemperatureUnit.Celsius): Location =
        runBlocking {
            val tempList = async { getCacheOrCallForecast(location, tempUnit) }
            Location(
                location = location,
                tempUnit = tempUnit,
                nextDaysTemp = tempList.await(),
            )
        }

    private suspend fun getCacheOrCallTemp(location: String, tempUnit: TemperatureUnit): Float {
        val kTemp = if (cacheTemp.containsKey(location)) {
            cacheTemp[location]!!
        } else {
            val temp = source.getLocationTemp(location).main.temp
            cacheTemp[location] = temp
            temp
        }
        return when (tempUnit) {
            TemperatureUnit.Celsius -> kToC(kTemp)
            TemperatureUnit.Fahrenheit -> kToF(kTemp)
        }
    }

    private suspend fun getCacheOrCallForecast(location: String, tempUnit: TemperatureUnit): List<Float> {
        val v = cacheForecast[location]
        val nextFiveDaysInKelvin = if (v != null && (now() - v.lastUpdate < ttlForecast)) {
            v.nextFiveDays
        } else {
            val forecastResponse = source.getForecast(location)
            val nextFiveDays = getNextFiveDaysFromForecastResponse(forecastResponse)

            cacheForecast[location] = LocationForecast(
                location = location,
                nextFiveDays = nextFiveDays,
                lastUpdate = now(),
            )
            nextFiveDays
        }
        return when (tempUnit) {
            TemperatureUnit.Celsius -> nextFiveDaysInKelvin.map { kToC(it) }
            TemperatureUnit.Fahrenheit -> nextFiveDaysInKelvin.map { kToF(it) }
        }
    }

    private fun kToC(k: Float) = (k - 273.15F)
    private fun kToF(k: Float) = ((k - 273.15F) * (9 / 5) + 32)
    private fun now() = System.currentTimeMillis()

    private fun getNextFiveDaysFromForecastResponse(forecastResponse: ForecastResponse): List<Float> {
        val todayNoonEpoch = LocalDateTime.of(LocalDate.now(), LocalTime.NOON).toEpochSecond(ZoneOffset.UTC)
        return listOf(
            forecastResponse.list.first { it.dt == todayNoonEpoch + (1 * (24 * 60 * 60)) }.main.temp,
            forecastResponse.list.first { it.dt == todayNoonEpoch + (2 * (24 * 60 * 60)) }.main.temp,
            forecastResponse.list.first { it.dt == todayNoonEpoch + (3 * (24 * 60 * 60)) }.main.temp,
            forecastResponse.list.first { it.dt == todayNoonEpoch + (4 * (24 * 60 * 60)) }.main.temp,
            forecastResponse.list.first { it.dt == todayNoonEpoch + (5 * (24 * 60 * 60)) }.main.temp,
        )
    }

    private data class LocationForecast(val location: String, val nextFiveDays: List<Float>, val lastUpdate: Long)
}
