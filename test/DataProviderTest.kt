package com.amerharb.shape

import com.amerharb.shape.models.TemperatureUnit
import kotlin.test.Test
import kotlinx.coroutines.*


class DataProviderTest {
    @Test //Note: this is not unit test, it is just used for debugging
    fun getLocationTemp() {
        runBlocking {
            launch { println(DataProvider.getLocationsTemp(listOf("Malmo"), TemperatureUnit.Celsius)) }
        }
    }
}
