package test

import java.time._
import java.time.format._
import org.scalatestplus.play._
import org.mockito.Mockito._
import play.api.libs.ws._
import play.api.libs.json.Json
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services._
import org.scalatest.concurrent.ScalaFutures

class ApplicationSpec extends PlaySpec with MockitoSugar with ScalaFutures {
    "DateTimeFormat" must {
        "return 1970 as the beginning of epoch" in {
            val beginning = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault())
            val formattedYear = beginning.format(DateTimeFormatter.ofPattern("YYYY")) 
            formattedYear mustBe "1970"
        }
    }
    "SunService" must {
        "retrieve correct sunset and runrise information" in {
            val wsClientStub = mock[WSClient]
            val wsRequestStub = mock[WSRequest]
            val wsResponseStub = mock[WSResponse]
            val lat = -33.8830
            val lon = 151.2167
            val url = s"http://api.sunrise-sunset.org/json?lat=$lat&lng=$lon&formatted=0"

            val expectedResponse = """
            {
                "results":{
                    "sunrise":"2016-04-14T20:18:12+00:00",
                    "sunset":"2016-04-15T07:31:52+00:00"
                }
            }
            """
            val jsResult = Json.parse(expectedResponse)
            when(wsResponseStub.json).thenReturn(jsResult)
            when(wsRequestStub.get()).thenReturn(
                Future.successful(wsResponseStub)
            )
            when(wsClientStub.url(url)).thenReturn(wsRequestStub)
            val sunService = new SunService(wsClientStub)
            val resultF = sunService.getSunInfo(lat, lon)
            whenReady(resultF) {res => 
                res.sunrise mustBe "13:18:12"
                res.sunset mustBe "00:31:52"
            }
        }
    }
}