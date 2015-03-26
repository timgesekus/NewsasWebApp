package controllers

import play.api._
import play.api.mvc._
import websocketactors.ChatWebSocket
import net.gesekus.newsaswebapp.views.ChatPresenter
import net.gesekus.newsaswebapp.actors.ChatActor
import net.gesekus.newsaswebapp.domainmodel.ChatId
import play.libs.Akka
import play.api.Play.current
import net.gesekus.newsaswebapp.domainmodel.inmemoryimpl.InMemoryConfig

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def chatSocket = WebSocket.acceptWithActor[String, String] { 
    request =>
    out =>
      val chatPresenter = Akka.system().actorOf(ChatPresenter.props())
      val chatActor = Akka.system().actorOf(ChatActor.props(InMemoryConfig))
      ChatWebSocket.props(out, chatPresenter, chatActor, "chat-1", "user-1s")
  }
}