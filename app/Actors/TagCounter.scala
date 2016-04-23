package Actors

import Actors.TagCounter.{GetCounts, Tag}
import akka.actor.{Actor, ActorSystem, Props}
import com.google.inject.Inject

class TagCounter () extends Actor{
  var count = Map[String, Int]()
  override def receive: Receive = {
    case Tag(tag, status) =>
      val old: Int = count.getOrElse(tag, 0)
      count = count + (tag -> (old + 1))
    case GetCounts() =>
      sender ! count
  }
}

object TagCounter {
  def props = Props[TagCounter]
  case class Tag(tag: String, status: String)
  case class GetCounts()
}
