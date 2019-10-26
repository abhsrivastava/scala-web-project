package models
import play.api.libs.json.Json

case class SunInfo(sunrise: String, sunset: String)
object SunInfo {
    implicit val writes = Json.writes[SunInfo]
}
case class CombinedData(sunInfo: SunInfo, temprature: Double, requests: Int)
object CombinedData {
    implicit val writes = Json.writes[CombinedData]
}