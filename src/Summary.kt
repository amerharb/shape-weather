package com.amerharb.shape

data class LocationTemp(val location: String, val temp: Int)
data class Summary(val tempUnit: TemperatureUnit, val locations: List<LocationTemp>)
