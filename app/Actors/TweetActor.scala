package Actors

import javax.inject.{Inject, Named}

import Actors.TagCounter.Tag
import Actors.TweetActor.Tweet
import akka.actor.{Actor, ActorRef, Props}

class TweetActor @Inject() (@Named("tag-counter-actor") tagCounter: ActorRef,
                            @Named("sentiment-actor") sentimentApi: ActorRef) extends Actor {
  override def receive: Receive = {
    case Tweet(status, follows) =>
      follows.foreach {
        tag => if (status.toLowerCase.contains(tag.toLowerCase)) {
          println(s"tweet is for tag $tag")
          val newTag = Tag(tag, status)
          tagCounter ! newTag
          sentimentApi ! Tag(tag, status)
        }
      }

  }
}

object TweetActor {
  def props = Props[TweetActor]
  case class Tweet(status: String, tags: Set[String])
}
