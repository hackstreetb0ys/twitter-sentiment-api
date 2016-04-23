package controllers


import javax.inject._

import Actors.TagCounter.GetCounts
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.json.Json
import play.api.mvc._
import services.Counter

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * This controller demonstrates how to use dependency injection to
 * bind a component into a controller class. The class creates an
 * `Action` that shows an incrementing count to users. The [[Counter]]
 * object is injected by the Guice dependency injection system.
 */
@Singleton
class CountController @Inject() (@Named("tag-counter-actor") counter: ActorRef) extends Controller {

  /**
   * Create an action that responds with the [[Counter]]'s current
   * count. The result is plain text. This `Action` is mapped to
   * `GET /count` requests by an entry in the `routes` config file.
   */
  def count = Action.async {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    implicit val t = Timeout(5 seconds)
    val futureCounts = ask(counter, GetCounts()).mapTo[Map[String, Int]]
    futureCounts.map { x => Ok(Json.toJson(x))}
  }

}
