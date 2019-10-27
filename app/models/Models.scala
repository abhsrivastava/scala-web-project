package models
import play.api.libs.json.Json
import scalikejdbc._
import java.util.UUID
import play.api.mvc.{Request, WrappedRequest}

case class SunInfo(sunrise: String, sunset: String)
object SunInfo {
    implicit val writes = Json.writes[SunInfo]
}
case class CombinedData(sunInfo: SunInfo, temprature: Double, requests: Int)
object CombinedData {
    implicit val writes = Json.writes[CombinedData]
}

case class User(uuid: UUID, userCode: String, password: String)
object User {
    def fromRS(rs: WrappedResultSet) : User = {
        User(UUID.fromString(rs.string("user_id")), rs.string("user_code"), rs.string("password"))
    }
}

case class UserLoginData(username:String,password:String)
case class UserAuthRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)