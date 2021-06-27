package com.amerharb.shape.openweathermap

data class ForecastResponse (val list:List<ForecastItem>, val city: City, val name: String)
data class City (val name:String, val country: String)
data class ForecastItem (val dt:Long, val main: Main)

