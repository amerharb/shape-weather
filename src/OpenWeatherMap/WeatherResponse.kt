package com.amerharb.shape.openweathermap

data class WeatherResponse (val coord:Coord, val main: Main, val name: String)
data class Coord(val lon:Float, val lat:Float)
data class Main(val temp:Float) //temp in Kelvin

//example of response
//{"coord":{"lon":13.0007,"lat":55.6059},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":294.01,"feels_like":293.8,"temp_min":291.96,"temp_max":296.55,"pressure":1018,"humidity":63,"sea_level":1018,"grnd_level":1017},"visibility":10000,"wind":{"speed":3.67,"deg":323,"gust":4.73},"clouds":{"all":95},"dt":1624714204,"sys":{"type":1,"id":1575,"country":"SE","sunrise":1624674358,"sunset":1624737334},"timezone":7200,"id":2692969,"name":"Malmo","cod":200}
//{"coord":{"lon":13.0007,"lat":55.6059},"main":{"temp":294.01}, "name":"Malmo"}
