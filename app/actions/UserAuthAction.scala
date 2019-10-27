package actions

import services._
import scala.concurrent.ExecutionContext
import play.api.mvc._
import models._
import scala.concurrent.Future

class UserAuthAction(authService: AuthService, ec: ExecutionContext, playBodyParsers: PlayBodyParsers) 
    extends ActionBuilder[UserAuthRequest, AnyContent] {
        override val executionContext: ExecutionContext = ec
        override def parser = playBodyParsers.defaultBodyParser
        def invokeBlock[A](request: Request[A], block: (UserAuthRequest[A]) => Future[Result]) : Future[Result] = {
            authService.checkCookie(request) match {
                case Some(user) => block(UserAuthRequest(user, request))
                case None => Future.successful(Results.Redirect("/login"))
            }
        }
}