package com.amerharb.shape.models

data class Summary(val tempUnit: TemperatureUnit, val locations: List<LocationTemp>)
data class LocationTemp(val location: String, val temp: Int)
