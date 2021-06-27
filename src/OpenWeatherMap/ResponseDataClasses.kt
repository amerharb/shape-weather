package com.amerharb.shape.openweathermap

data class WeatherResponse (val coord:Coord, val main: Main, val name: String)

data class ForecastResponse (val list:List<ForecastItem>, val city: City, val name: String)

data class Coord(val lon:Float, val lat:Float)
data class ForecastItem (val dt:Long, val main: Main)
data class City (val name:String, val country: String)
data class Main(val temp:Float) //temp in Kelvin

