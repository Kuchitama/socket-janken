package models

import scala.util.Random

object Rival {
  def figure:Janken.Figure = Random.shuffle(Janken.Figure.values).head
}
