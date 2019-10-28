package test

import java.time._
import java.time.format._
import org.scalatestplus.play._
import org.mockito.Mockito._
import play.api.libs.ws._

class ApplicationSpec extends PlaySpec {
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
            val wsResponseStub = mock[WSReponse]
            val expectedResponse = """
            {
                "results":{
                    "sunrise":"2016-04-14T20:18:12+00:00",
                    "sunset":"2016-04-15T07:31:52+00:00"
                }
            }
            """
        }
    }
}