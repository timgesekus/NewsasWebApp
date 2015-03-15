package net.gesekus.newsaswebapp.domainmodel

import scala.util.{Success, Failure, Try}
import scalaz.ReaderT
import scalaz.contrib.std.utilTry._

/**
 * Created by tim on 15.03.2015.
 */
trait ChatService[Chat, ChatMessage, UserId, ChatId] {
  def open(owner: UserId, chatId: ChatId) : ReaderT[Try, Config, Chat]

  def close(chatId: ChatId) : ReaderT[Try, Config, ChatId]

  def addMessage(chatId: ChatId, chatMessage: ChatMessage) : ReaderT[Try, Config, Chat]
}

object ChatService extends ChatService[Chat, ChatMessage, UserId, ChatId] with Chats {
  override 
  def open(ownerId: UserId, chatId: ChatId) = {
    for {
      validatedChatId <- validateChatId(chatId)
      newChat <- store(Chat(chatId, ownerId))
    } yield (newChat)
  }
  
  override 
  def close(chatId: ChatId) = {
    for {
      chatId <- remove(chatId)
    } yield (chatId)
  }

  override 
  def addMessage(chatId: ChatId, chatMessage: ChatMessage)  =
    for {
      chat <- find(chatId)
      storedChat <- store(chat.copy(messages = chatMessage :: chat.messages))
    } yield (storedChat)
}
