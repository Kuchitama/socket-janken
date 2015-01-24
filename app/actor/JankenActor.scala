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
      val result = Janken.Figure.withName(msg) vs Rival.figure
      out ! s"You ${result.name.toLowerCase}!"
    }
    case msg : String => out ! msg
  }

}
