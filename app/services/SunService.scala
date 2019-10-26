package services

import play.api.libs.ws.WSClient

// time imports
import java.time.{ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import models._

class SunService(ws: WSClient) {
    def getSunInfo(lat: Double, lon: Double) : Future[SunInfo] = {
        val sunUrl = s"http://api.sunrise-sunset.org/json?lat=$lat&lng=$lon&formatted=0"
        println(sunUrl)
        ws.url(sunUrl).get().map{resp => 
            val sunJson = resp.json
            val sunriseTimeStr = (sunJson \ "results" \ "sunrise").as[String]
            val sunsetTimeStr = (sunJson \ "results" \ "sunset").as[String]
            val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
            val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneOffset.ofHours(-7))
            SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
        }
    }
}