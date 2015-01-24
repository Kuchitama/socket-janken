package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import actor.JankenActor

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    JankenActor.props(out)
  }

}