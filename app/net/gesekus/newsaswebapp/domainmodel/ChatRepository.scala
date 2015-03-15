package net.gesekus.newsaswebapp.domainmodel

import scala.util.{Try, Success, Failure}

/**
 * Created by tim on 15.03.2015.
 */

trait ChatRepository {
  def find(chatId: ChatId): Try[Chat]
  def store(chat: Chat) : Try[Chat]
  def remove(chatId: ChatId) : Try[ChatId]
  def contains(chatId: ChatId) : Boolean
}

object  ChatRepository extends ChatRepository {
  var chats: Map[ChatId, Chat] = Map()

  def find(chatId: ChatId): Try[Chat] = {
    chats.get(chatId).map(Success.apply(_)).getOrElse(chatIdNotFoundFailure(chatId))
  }

  def store(chat: Chat) = {
    chats = chats + (chat.id -> chat)
    Success(chat)
  }

  def remove(chatId: ChatId) = {
    if (contains(chatId)) {
      chats = chats - chatId
      Success(chatId)
    } else {
      chatIdNotFoundFailure(chatId)
    }
  }

  def contains(chatId: ChatId) = {
    chats.contains(chatId)
  }

  private def chatIdNotFoundFailure(chatId: ChatId) = {
    Failure(new IllegalArgumentException("No chat found"))
  }
}
