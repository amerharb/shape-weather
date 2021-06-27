package com.amerharb.shape.models

data class Location(val location: String, val tempUnit: TemperatureUnit, val nextFiveDaysTemp: List<Float>)
