package Actors

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
  * Created by rag on 23/04/2016.
  */
class TweetActor extends Actor {
  override def receive: Receive = {
    case _ =>
  }
}

object TweetActor {
  case class Tweet(status: String)
}
