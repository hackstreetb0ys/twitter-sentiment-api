package controllers

import javax.inject.Inject

import Actors.TwitterStreamActor
import Actors.TwitterStreamActor.Follow
import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.Singleton
import play.api.mvc.{Action, Controller}

@Singleton
class FollowController @Inject() (system: ActorSystem) extends Controller{

  val twitterStreamActor = {
    system.actorOf(TwitterStreamActor.props)
  }

  def followTags() = Action { request =>
    twitterStreamActor ! Follow(Set("anything"))
    Ok("stub");
  }

  def unfollowTag(tag: String) = Action { request =>
    Ok("stub");
  }
}
