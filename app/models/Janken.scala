package models

package Janken {
  object Figure {
    val values: List[Figure] = List(Gu, Choki, Pa)
    def withName(name: String): Figure = withNameOpt(name).getOrElse(throw new IllegalArgumentException(s"Argument[${name}] is not name of Janken Figure."))
    def withNameOpt(name: String): Option[Figure] = values.find(_.name == name)
    case object Gu extends Figure
    case object Choki extends Figure
    case object Pa extends Figure

  }
  sealed trait Figure {
    val name = toString
    def vs(rival: Figure): JankenResult = {
      import Figure._
      import Result._
      val res = this match {
        case Gu     => if (rival == Choki) Win  else if(rival == Pa) Lose     else Draw
        case Choki  => if (rival == Pa) Win     else if(rival == Gu) Lose     else Draw
        case Pa     => if (rival == Gu) Win     else if(rival == Choki) Lose  else Draw
      }

      JankenResult(this, rival, res)
    }

  }

  case class JankenResult(you: Figure, rival: Figure, result: Result)

  object Result {
    val values: List[Result] = List(Win, Lose)

    case object Win extends Result
    case object Lose extends Result
    case object Draw extends Result
  }
  sealed trait Result { val name = toString }
}
