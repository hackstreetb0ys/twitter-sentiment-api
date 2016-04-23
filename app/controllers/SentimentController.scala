package controllers

import models.TweetSentiment
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class SentimentController extends Controller {

  val stubSentiments = List(
    TweetSentiment("#HackSussex", 100, 0)
    , TweetSentiment("#SomeOtherTag", 0, 10000)
  )

  def getSentiments() = Action {
    Ok(Json.toJson(stubSentiments));
  }
}
