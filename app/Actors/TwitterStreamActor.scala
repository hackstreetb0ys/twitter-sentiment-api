package Actors

import javax.inject.{Inject, Named}

import Actors.TweetActor.Tweet
import Actors.TwitterStreamActor.{Clean, Connected, Follow, State, Unfollow}
import akka.actor.{Actor, ActorRef, Props}
import com.google.inject.Singleton
import twitter4j._

import collection.JavaConverters._

@Singleton
class  TwitterStreamActor @Inject() (@Named("tweet-actor") tweetActor: ActorRef) extends Actor with TwitterAPI {
  val twitterStream = new TwitterStreamFactory(config).getInstance
  var tags: Set[String]  = Set()
  var state: State = Clean()
  var listenerRef: StatusListener = null


  override def receive = {
    case Follow(newTags) =>
      tags = tags ++ newTags
      state match {
        case Clean() => startAPI()
          println("starting")
          state = Connected()
        case Connected() => restartAPI()
      }

    case Unfollow(oldtag) =>
      tags = tags - oldtag

      state match {
        case Clean() => startAPI()
          state = Connected()
        case Connected() => restartAPI()
      }
  }

  private def startAPI() = {
    if (tags.nonEmpty){
      var listenerRef = listener
      twitterStream.addListener(listenerRef)
      val query = new FilterQuery()
      val tagArray: Array[String] = tags.toArray
      query.track(tagArray: _*)
      twitterStream.filter(query)
      println("connected")
    }
  }

  private def restartAPI() = {
    if (tags.nonEmpty){
      val query = new FilterQuery()
      query.track(tags.toArray:_*)
      twitterStream.filter(query)
      println("updating query")
    } else {
      twitterStream.cleanUp()
      twitterStream.shutdown()
      state = Clean()
    }
  }

  override def postStop(): Unit = {
    twitterStream.cleanUp()
    twitterStream.shutdown()
  }

  def listener = new StatusListener() {
    def onStatus(status: Status) { println("got tweet"); tweetActor ! Tweet(status.getText, tags)}
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }

}

object TwitterStreamActor {
  def props = Props[TwitterStreamActor]
  //State
  trait State
  case class Clean() extends State
  case class Connected() extends State

  //messages
  case class Follow(tags: Set[String])
  case class Unfollow(tag: String)
}

