package net.gesekus.newsaswebapp.actors

import akka.actor.{ Actor, ActorRef }
import akka.actor.ActorLogging
import net.gesekus.newsaswebapp.domainmodel._
import akka.actor.Props
import scala.util.Success
import scala.util.Failure

sealed trait ChatCommand
case class OpenChat(chatId: ChatId, userId: UserId) extends ChatCommand
case class CloseChat(chatId: ChatId) extends ChatCommand
case class AddMessage(chatId: ChatId, chatMessage: ChatMessage) extends ChatCommand

sealed trait ChatEvent
case class ChatOpend(chat: Chat) extends ChatEvent
case class ChatClosed(chatId: ChatId) extends ChatEvent
case class MessageAdded(chatId: ChatId, chatMessage: ChatMessage) extends ChatEvent

class ChatActor(config: Config) extends Actor with ActorLogging with Subscribable {
  val chatService = ChatService

  def receive = {
    case Subscribe => addSubscriber
    case Unsubscribe => removeSubscriber
    case openChatMessage: OpenChat => openChat(openChatMessage)
    case closeChatMessage: CloseChat => closeChat(closeChatMessage)
    case addMessageMessage: AddMessage => addMessage(addMessageMessage)
  }

  def openChat(openChat: OpenChat) {
    val chatTry = chatService.open(openChat.userId, openChat.chatId).run(config)
    chatTry match {
      case Success(chat) => send(ChatOpend(chat))
      case Failure(e) => log.error(e, "Failed to open a chat")
    }
  }

  def closeChat(closeChatMessage: CloseChat) {
    val chatTry = chatService.close(closeChatMessage.chatId).run(config)
    chatTry match {
      case Success(chatId) => send(ChatClosed(chatId))
      case Failure(e) => log.error(e, "Failed to close a chat")
    }
  }
  def addMessage(addMessageMessage: AddMessage) {
    val chatId = addMessageMessage.chatId
    val chatMessage = addMessageMessage.chatMessage
    val chatTry = chatService.addMessage(chatId, chatMessage).run(config)
    chatTry match {
      case Success(chat) => send(MessageAdded(chatId, chatMessage))
      case Failure(e) => log.error(e, "Failed to close a chat")
    }
  }
}

object ChatActor {
  def props(config: Config): Props = Props(new ChatActor(config))
}