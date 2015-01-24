package actor

import akka.actor._
import models.{Rival, Janken}

object JankenActor {
  def props(out: ActorRef) = Props(new JankenActor(out))
}
class JankenActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String if msg == "close" => {
      out ! s"shutdow"
      self ! PoisonPill
    }
    case msg: String if Janken.Figure.withNameOpt(msg).isDefined => {
      val res = Janken.Figure.withName(msg) vs Rival.figure
      out ! s"You ${res.result.name.toLowerCase}! You: ${res.you} Rival: ${res.rival}"
    }
    case msg : String => out ! msg
  }

}
