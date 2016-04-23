package Actors

import Actors.TwitterStreamActor.Follow
import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}

@Singleton
class System  @Inject() (system: ActorSystem){
  val twitterStreamActor = {
    println("starting api actor")
    system.actorOf(TwitterStreamActor.props)
  }
  val start = twitterStreamActor ! Follow(Set("anything"))
}
