package net.gesekus.newsaswebapp.views

import akka.actor.Actor
import net.gesekus.newsaswebapp.actors.ChatOpend
import net.gesekus.newsaswebapp.actors.ChatMessageAdded
import net.gesekus.newsaswebapp.actors.ChatClosed
import net.gesekus.newsaswebapp.domainmodel.ChatMessage
import scala.collection._
import net.gesekus.newsaswebapp.domainmodel.ChatId
import akka.actor.ActorRef
import mutable.{Set, MultiMap, HashMap }
import akka.actor.Props


sealed trait ChatPresenterMessages
case class Subscribe(chatid: ChatId) extends ChatPresenterMessages
case class Unsubscribe(chatid: ChatId) extends ChatPresenterMessages

sealed trait ChatPresenterEvents
case class ChatUpdated(chatId: ChatId, messages: List[String])

object ChatPresenter {
  def props() = Props(new ChatPresenter())
}
class ChatPresenter extends Actor {

  val chats: mutable.Map[ChatId, Pipe] = mutable.Map()
  val subscribers = new HashMap[ChatId, Set[ActorRef]] with MultiMap [ChatId, ActorRef]
  def receive = {
    case Subscribe(chatId) => {
      subscribers.addBinding(chatId, sender())
    }
    
    case Unsubscribe(chatId) => {
      subscribers.removeBinding(chatId, sender())
    }
    case ChatOpend(chat) => {
      chats + (chat.id -> new Pipe(20))
    }
    case ChatClosed(chatId) => {
      chats - chatId
    }
    case ChatMessageAdded(chatId, chatMessage) => {
      chats(chatId).addMessage(chatMessage)
      presentToSubscribers(chatId, chats(chatId).getMessages())
    }
  }

  def presentToSubscribers(chatId: ChatId, chatMessages: List[ChatMessage]) = {
    val presentableMessages = chatMessages.map(message => message.userId + ">" + message.message)
    val updatedChat = ChatUpdated (chatId, presentableMessages)
    subscribers(chatId).foreach { actor => actor.tell(updatedChat, self) }
  }
  
  
}

class Pipe(size: Int) {
  var elements: immutable.List[ChatMessage] = List()

  def addMessage(chatMessage: ChatMessage): Unit = {
    elements = chatMessage :: elements.take(size)
  }
  
  def getMessages() : List[ChatMessage] = {
    elements
  } 

}
