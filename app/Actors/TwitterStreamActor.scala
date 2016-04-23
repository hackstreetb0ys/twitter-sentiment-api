package Actors

import Actors.TweetActor.Tweet
import Actors.TwitterStreamActor.{Clean, Connected, Follow, State, Unfollow}
import akka.actor.{Actor, ActorRef, Props}
import com.google.inject.{Inject, Singleton}
import twitter4j._

import collection.JavaConverters._

@Singleton
class  TwitterStreamActor @Inject() (analyzer: ActorRef) extends Actor with TwitterAPI{
  val twitterStream = new TwitterStreamFactory(config).getInstance
  var tags: Set[String]  = Set()
  var state: State = Clean()


  override def receive = {
    case Follow(newTags) =>
      tags = tags ++ newTags
      state match {
        case Clean() => startAPI()
          println("starting")
          state = Connected()
        case Connected() => restartAPI()
      }
      // restart api
    case Unfollow(oldtags) =>
      tags = tags -- oldtags
      //restart api
      state match {
        case Clean() => startAPI()
          state = Connected()
        case Connected() => restartAPI()
      }
  }

  private def startAPI() = {
    twitterStream.addListener(listener)
    val taggs = tags.toArray
    val javaTagSet = tags.asJava
    val tagArray: Array[java.lang.String] = javaTagSet.toArray(new Array[java.lang.String](tags.size))
//    twitterStream.filter(new FilterQuery(Array("#twitter")))
    println("connected")

  }

  private def restartAPI() = {

  }

  def listener = new StatusListener() {
//    def onStatus(status: Status) { analyzer ! Tweet(status.getText) }
    def onStatus(status: Status) { println(status.getText) }
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
  case class Unfollow(tags: Set[String])
}

