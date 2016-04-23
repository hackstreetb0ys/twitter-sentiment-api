package controllers

import javax.inject.{Inject, Named}

import Actors.TwitterStreamActor
import Actors.TwitterStreamActor.{Follow, Unfollow}
import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.Singleton
import play.api.libs.json.{JsError, JsSuccess}
import play.api.mvc.{Action, BodyParsers, Controller}

@Singleton
class FollowController @Inject() (@Named("stream-actor") twitterStreamActor: ActorRef) extends Controller{

//  val twitterStreamActor = system.actorOf(TwitterStreamActor.props)


  def followTags() = Action(BodyParsers.parse.json)  { request =>
    request.body.validate[Set[String]] match {
      case JsSuccess(tagSet, path) => twitterStreamActor ! Follow(tagSet)
      case JsError(e) => BadRequest("Error")
    }

    Ok("stub");
  }

  def unfollowTag(tag: String) = Action { request =>
    twitterStreamActor ! Unfollow(tag)
    Ok("stub");
  }
}
