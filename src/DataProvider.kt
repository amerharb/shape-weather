package com.amerharb.shape

import com.amerharb.shape.models.LocationTemp
import com.amerharb.shape.models.Summary
import com.amerharb.shape.models.TemperatureUnit
import com.amerharb.shape.openweathermap.OpenWeatherMapClient
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 *  DataProvider is single instance object that
 *  1. take data from open weather map on demand
 *  2. cache it for next calls
 *  3. convert Kelvin to Cel or Feh
 */
object DataProvider {
    private val source = OpenWeatherMapClient()
    private val cacheTemp = mutableMapOf<String, Float>()

    fun getLocationsTemp(locations: List<String>, tempUnit: TemperatureUnit): Summary =
        runBlocking<Summary> {
            val callList = locations.map { it to async { getCacheOrCallTemp(it, tempUnit) } }
            val locationTemps = callList.map { LocationTemp(it.first, it.second.await()) }
            Summary(tempUnit, locationTemps)
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
            TemperatureUnit.Celsius -> (kTemp - 273.15F)
            TemperatureUnit.Fahrenheit -> ((kTemp - 273.15F) * (9 / 5) + 32)
        }
    }
}
