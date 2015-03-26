package websocketactors

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Actor
import net.gesekus.newsaswebapp.views.Subscribe
import net.gesekus.newsaswebapp.views.ChatUpdated
import play.api.libs.json._
import net.gesekus.newsaswebapp.actors.AddChatMessage
import net.gesekus.newsaswebapp.domainmodel.ChatMessage
import net.gesekus.newsaswebapp.domainmodel.UserId
import net.gesekus.newsaswebapp.domainmodel.ChatId
import net.gesekus.newsaswebapp.actors.AddChatMessage

case class NewMessage(message: String)

object ChatWebSocket {
  def props(out: ActorRef, chatPresenter: ActorRef, chatActor: ActorRef, chatId: String, user: String) =
    Props(new ChatWebSocket(out, chatPresenter, chatActor, chatId, user))
}

class ChatWebSocket(out: ActorRef, chatPresenter: ActorRef, chatActor: ActorRef, aChatId: String, aUserId: String) extends Actor {
  import JsonConverter._
  val userId = UserId(aUserId)
  val chatId = ChatId(aChatId)
  chatPresenter.tell(Subscribe(chatId), self)
  def receive = {
    case msg: String => {
      val message = Json.parse(msg)
      val event = message \ "event"
      event match {
        case newMessage => {
          val newMessage = Json.fromJson[NewMessage](message).get
          val chatMessage = ChatMessage(userId, newMessage.message)
          chatActor.tell(AddChatMessage(chatId, chatMessage), self)
        }
      }
    }
    case updatedChat: ChatUpdated => {
      val jsonMessage = Json.toJson(updatedChat)
      out.tell(Json.stringify(jsonMessage), sender)
    }
  }
}