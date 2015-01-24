package actor

import akka.actor._
import models.Janken._
import models.Rival

object JankenActor {
  def props(out: ActorRef) = Props(new JankenActor(out))
}
class JankenActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String if msg == "close" => {
      out ! s"shutdow"
      self ! PoisonPill
    }
    case msg: String if Figure.withNameOpt(msg).isDefined => {
      val res = Figure.withName(msg) vs Rival.figure
      out ! s"You ${res.result.name.toLowerCase}! You: ${res.you} Rival: ${res.rival}"
    }
    case msg : String => out ! msg
  }

}
