package filters

import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}
import scala.concurrent.Future
import akka.actor.ActorSystem
import play.api.Logger
import models._
import actors._

class StatsFilter(actorSystem: ActorSystem, implicit val mat: Materializer) extends Filter {
    private val log = Logger(this.getClass)
    override def apply(nextFilter: RequestHeader => Future[Result])(header: RequestHeader) : Future[Result] = {
        actorSystem.actorSelection(StatsActor.path) ! RequestReceived
        nextFilter(header)
    }
}