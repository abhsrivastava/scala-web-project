package tests

import play.api.routing.Router
import services.WeatherService
import play.api.NoHttpFiltersComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext

class TestAppComponents(context: Context) 
    extends BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents
    with AhcWSComponents {
    lazy val router: Router = Router.empty
    lazy val weatherService = wire[WeatherService]
}