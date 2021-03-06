import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import services._
import scala.concurrent.Future
import filters._
import models._
import akka.actor.Props
import actors._
import scalikejdbc.config.DBs
import play.api.db.evolutions.EvolutionsComponents
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.cache.caffeine.CaffeineCacheComponents
import actions._

class AppApplicationLoader extends ApplicationLoader {
    def load(context: Context) = {
        LoggerConfigurator(context.environment.classLoader).foreach{cfg => cfg.configure(context.environment)}
        new AppComponents(context).application
    }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) 
    with AhcWSComponents with EvolutionsComponents 
    with DBComponents with HikariCPComponents 
    with AssetsComponents with CaffeineCacheComponents 
    with HttpFiltersComponents {
        private val log = Logger(this.getClass)
        lazy val statsActor = actorSystem.actorOf(Props(wire[StatsActor]), StatsActor.name)
        //lazy val dynamicEvolutions = new DynamicEvolutions

        val onStart = {
            log.info("""  __  __        __          __             _            __       _                        _ _           _   _             """)
            log.info(""" |  \/  |       \ \        / /            | |          / _|     | |     /\               | (_)         | | (_)            """)
            log.info(""" | \  / |_   _   \ \  /\  / /__  _ __   __| | ___ _ __| |_ _   _| |    /  \   _ __  _ __ | |_  ___ __ _| |_ _  ___  _ __  """)
            log.info(""" | |\/| | | | |   \ \/  \/ / _ \| '_ \ / _` |/ _ \ '__|  _| | | | |   / /\ \ | '_ \| '_ \| | |/ __/ _` | __| |/ _ \| '_ \ """)
            log.info(""" | |  | | |_| |    \  /\  / (_) | | | | (_| |  __/ |  | | | |_| | |  / ____ \| |_) | |_) | | | (_| (_| | |_| | (_) | | | |""")
            log.info(""" |_|  |_|\__, |     \/  \/ \___/|_| |_|\__,_|\___|_|  |_|  \__,_|_| /_/    \_\ .__/| .__/|_|_|\___\__,_|\__|_|\___/|_| |_|""")
            log.info("""          __/ |                                                              | |   | |                                    """)
            log.info("""         |___/                                                               |_|   |_|                                    """)
            statsActor ! Ping
            DBs.setupAll()
            applicationEvolutions
        }

        applicationLifecycle.addStopHook{() => 
            log.info("""  ____               ____             """)
            log.info(""" |  _ \             |  _ \            """)
            log.info(""" | |_) |_   _  ___  | |_) |_   _  ___ """)
            log.info(""" |  _ <| | | |/ _ \ |  _ <| | | |/ _ \""")
            log.info(""" | |_) | |_| |  __/ | |_) | |_| |  __/""")
            log.info(""" |____/ \__, |\___| |____/ \__, |\___|""")
            log.info("""         __/ |              __/ |     """)
            log.info("""        |___/              |___/      """)
            DBs.closeAll()
            Future.successful(Unit)
        }
        
        override lazy val controllerComponents = wire[DefaultControllerComponents]
        lazy val userAuthAction = wire[UserAuthAction]
        lazy val authService = new AuthService(defaultCacheApi.sync)
        lazy val sunService = wire[SunService]
        lazy val weatherService = wire[WeatherService]
        lazy val statsFilter: Filter = wire[StatsFilter]
        lazy val prefix: String = "/"
        lazy val router: Router = wire[Routes]
        lazy val applicationController = wire[Application]
        override lazy val httpFilters = Seq(statsFilter)
}
