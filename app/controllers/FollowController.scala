package controllers

import javax.inject.Inject

import Actors.TwitterStreamActor.Follow
import akka.actor.ActorRef
import com.google.inject.name.Named
import play.api.mvc.{Action, Controller}

class FollowController @Inject() (@Named("twitter-api") apiActor: ActorRef) extends Controller{

  def followTags() = Action { request =>
    apiActor ! Follow(Set("anything"))
    Ok("stub");
  }

  def unfollowTag(tag: String) = Action { request =>
    Ok("stub");
  }
}
