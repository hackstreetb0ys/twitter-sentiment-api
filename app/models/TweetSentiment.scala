package models

import play.api.libs.json.Json

case class TweetSentiment(tag: String, positive: Int, negative: Int)

object TweetSentiment {
  implicit val tweetSentimentFormat = Json.format[TweetSentiment]
}
