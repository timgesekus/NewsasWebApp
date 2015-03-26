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
case class AddChatMessage(chatId: ChatId, chatMessage: ChatMessage) extends ChatCommand

sealed trait ChatEvent
case class ChatOpend(chat: Chat) extends ChatEvent
case class ChatClosed(chatId: ChatId) extends ChatEvent
case class ChatMessageAdded(chatId: ChatId, chatMessage: ChatMessage) extends ChatEvent

class ChatActor(config: Config) extends Actor with ActorLogging with Subscribable {
  val chatService = ChatService

  def receive = {
    case Subscribe => addSubscriber
    case Unsubscribe => removeSubscriber
    case openChat: OpenChat => processOpenChat(openChat)
    case closeChat: CloseChat => processCloseChat(closeChat)
    case addChatMessage: AddChatMessage => processAddChatMessage(addChatMessage)
  }

  def processOpenChat(openChat: OpenChat) {
    val chatTry = chatService.open(openChat.userId, openChat.chatId).run(config)
    chatTry match {
      case Success(chat) => send(ChatOpend(chat))
      case Failure(e) => log.error(e, "Failed to open a chat")
    }
  }

  def processCloseChat(closeChatMessage: CloseChat) {
    val chatTry = chatService.close(closeChatMessage.chatId).run(config)
    chatTry match {
      case Success(chatId) => send(ChatClosed(chatId))
      case Failure(e) => log.error(e, "Failed to close a chat")
    }
  }
  
  def processAddChatMessage(addMessageMessage: AddChatMessage) {
    val chatId = addMessageMessage.chatId
    val chatMessage = addMessageMessage.chatMessage
    val chatTry = chatService.addMessage(chatId, chatMessage).run(config)
    chatTry match {
      case Success(chat) => send(ChatMessageAdded(chatId, chatMessage))
      case Failure(e) => log.error(e, "Failed to close a chat")
    }
  }
}

object ChatActor {
  def props(config: Config): Props = Props(new ChatActor(config))
}