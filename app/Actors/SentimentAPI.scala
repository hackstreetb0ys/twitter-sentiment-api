package Actors

import javax.inject.Inject

import Actors.SentimentAPI._
import Actors.TagCounter.Tag
import Actors.TwitterStreamActor.Unfollow
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem, Cancellable, Props}
import com.google.inject.Singleton
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.ws.{WSClient, WSRequest}


object SentimentAPI {
  def props = Props[TagCounter]
  case class SentimentCheck(id: Int, text: String)
  object SentimentCheck {
    implicit val sentimentCheckFormat = Json.format[SentimentCheck]
  }
  case class SentimentQuery(documents: List[SentimentCheck])
  object SentimentQuery {
    implicit val sentimentQueryFormat = Json.format[SentimentQuery]
  }
  case class SentimentResult(id: String, score: Double)
  object SentimentResult {
    implicit val sentimentResultFormat = Json.format[SentimentResult]
  }
  case class SentimentCount(positive: Int, negative: Int)
  object SentimentCount {
    implicit val sentimentCountFormat = Json.format[SentimentCount]
  }
  case class CheckSentiments()
  case class GetSentiments()
}


@Singleton
 class SentimentAPI @Inject() (ws: WSClient, system: ActorSystem) extends Actor{

  var sentiments = Map[String, SentimentCount]()
  val request: WSRequest = ws.url("https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment")
    .withHeaders("ocp-apim-subscription-key" -> "dbfb3398ce7b453889782226675c9f75")
  var tags: List[SentimentCheck] = List()
  private var scheduler: Cancellable = _
  var ids = Map[Int, String]()
  var id = 0

  override def receive = {

    case Tag(tag, status) =>
      println(s"Adding $tag to next sentiment check batch")
      ids = ids + (id -> tag)
      tags = SentimentCheck(id, status) :: tags
      id = id + 1
    case CheckSentiments() =>
      if(!tags.isEmpty){
        println("sending sentiment check")
        import play.api.libs.concurrent.Execution.Implicits.defaultContext
//        val ids_ = ids
        val tagsToSend = tags
        tags = List[SentimentCheck]()
        val result = request.post(Json.toJson(SentimentQuery(tagsToSend)))
          .map {
            response => (response.json \ "documents").validate[List[SentimentResult]]
              .fold({
                error => println(error)
              }, {
                list => list.foreach {
                  res =>
                    ids.get(res.id.toInt).fold({
                      println("tag deleted")
                    }) ({ tag =>
                      if (res.score > 0.5) {
                        println("got +ve sentiment")
                        val oldVal = sentiments.getOrElse(tag, SentimentCount(0,0))
                        sentiments = sentiments + (tag -> SentimentCount(oldVal.positive + 1, oldVal.negative))
                      } else {
                        println("got -ve sentiment")
                        val oldVal = sentiments.getOrElse(tag, SentimentCount(0,0))
                        sentiments = sentiments + (tag -> SentimentCount(oldVal.positive, oldVal.negative + 1))
                      }
                    })
                }
              })
          }
      }
    case GetSentiments() =>
      val sentimentsToSend = sentiments
      sender ! sentimentsToSend
    case Unfollow(tag) =>
      sentiments = sentiments - tag
      ids = ids.filter({case (x,t) => if (t == tag) false else true})
  }

  override def preStart() = {
    import scala.concurrent.duration._
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    scheduler = context.system.scheduler.schedule(
      initialDelay = 10 seconds,
      interval = 10 seconds,
      receiver = self,
      message = CheckSentiments()
    )
  }

  override def postStop(): Unit = {
    scheduler.cancel()
  }
}

