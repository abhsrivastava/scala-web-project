package tests

import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import org.scalatestplus.play._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._

class WeatherServiceSpec extends PlaySpec 
    with OneAppPerSuiteWithComponents with ScalaFutures {

        override def components = new TestAppComponents(context)
        override implicit val patienceConfig = PatienceConfig(timeout = Span(5, Seconds))

        "WeatherService" must {
            "return a meaningful temprature" in {
                val lat = -33.8830
                val lon = 151.22167
                val resultF = components.weatherService.getTemprature(lat, lon)
                whenReady(resultF) {result => 
                    result mustBe >=(-20.0)
                    result mustBe <=(70.0)
                }
            }
        }
}