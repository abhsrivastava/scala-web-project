package services
import models._
import play.api.cache.SyncCacheApi
import play.api.mvc.Cookie
import java.util.{Base64, UUID}
import java.security.MessageDigest
import scalikejdbc._
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.duration._
import play.api.mvc.RequestHeader

class AuthService(cacheApi: SyncCacheApi) {
    private val mda = MessageDigest.getInstance("SHA-512")
    private val cookieHeader = "X-Auth-Token"
    
    def checkCookie(header: RequestHeader) : Option[User] = {
        for {
            cookie <- header.cookies.get(cookieHeader)
            user <- cacheApi.get[User](cookie.value)
        } yield user
    }

    def login(userCode: String, password: String) : Option[Cookie] = {
        for {
            user <- checkUser(userCode, password)
            cookie = createCookie(user)
        } yield cookie
    }
    
    def checkUser(userCode: String, password: String) : Option[User] = {
        val mayBeUser = DB.readOnly{implicit session => 
            sql"""select * from users where user_code=$userCode""".map(User.fromRS).single().apply()
        }
        mayBeUser.flatMap{user => 
            if (BCrypt.checkpw(password, user.password)) {Some(user)} else None
        }
    }

    def createCookie(user: User) : Cookie = {    
        val randomPart = UUID.randomUUID().toString.toUpperCase
        val userPart = user.userCode.toString.toUpperCase
        val key = s"$randomPart|$userPart"
        val token = Base64.getEncoder().encodeToString(mda.digest(key.getBytes))
        val duration = Duration(10, HOURS)
        cacheApi.set(token, user, duration)
        Cookie(cookieHeader, token, maxAge = Some(duration.toSeconds.toInt))
    }
}