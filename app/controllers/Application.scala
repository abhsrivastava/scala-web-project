package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WSClient
import services._
import models._
import actors._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import akka.actor.ActorSystem
import play.api.libs.json.Json

class Application @Inject() (
  components: ControllerComponents, 
  assets: Assets, 
  sunService: SunService,
  weatherService: WeatherService,
  actorSystem: ActorSystem
  ) extends AbstractController(components) {
  
  def index = Action.async {
    Future.successful(Ok(views.html.index()))
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
  
  def data = Action.async {
    println("came inside data")
    val latitude = 47.6062d
    val longitude = -122.3321d
    val sunF = sunService.getSunInfo(latitude, longitude)
    val weatherF = weatherService.getTemprature(latitude, longitude)
    implicit val timeout = Timeout(5 seconds)
    val statsFuture : Future[Int] = (actorSystem.actorSelection(StatsActor.path) ? GetStats) .mapTo[Int]
    for {
      sunInfo <- sunF
      temprature <- weatherF
      requests <- statsFuture
    } yield {
      Ok(Json.toJson(CombinedData(sunInfo, temprature, requests)))
    }
  }
}
