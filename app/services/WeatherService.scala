package services

import play.api.libs.ws.WSClient
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService(ws: WSClient) {
    def getTemprature(lat: Double, lon: Double) : Future[Double] = {
        val appId = "851823af7bbd832c52670f41c6bf4c74"
        val weatherUrl = s"http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=imperial&APPID=$appId"
        ws.url(weatherUrl).get().map{resp => 
            val weatherJson = resp.json
            (weatherJson \ "main" \ "temp").as[Double]
        }
    }
}