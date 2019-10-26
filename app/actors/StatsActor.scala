package actors

import models._
import akka.actor._

class StatsActor extends Actor {
    var counter = 0    
    override def receive: Receive = {
        case Ping => ()
        case RequestReceived => counter += 1
        case GetStats => sender() ! counter
    }
}

object StatsActor {
    val name = "StatsActor"
    val path = s"/user/$name"
}