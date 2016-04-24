package controllers

import javax.inject.{Inject, Named}

import Actors.SentimentAPI.{GetSentiments, SentimentCount}
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Singleton
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.duration._

@Singleton
class SentimentController @Inject() (@Named("sentiment-actor") sentimentActor: ActorRef) extends Controller {

  def getSentiments = Action.async {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    implicit val t = Timeout(5 seconds)
    val futureCounts = ask(sentimentActor, GetSentiments()).mapTo[Map[String, SentimentCount]]
    futureCounts.map { x => Ok(Json.toJson(x)).withHeaders("Access-Control-Allow-Origin" -> "*")}
  }

}
