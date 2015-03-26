package net.gesekus.newsaswebapp.domainmodel.inmemoryimpl

import net.gesekus.newsaswebapp.domainmodel._
import scala.util.Success
import scala.util.Try
import scala.util.Failure


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
    if (chats.contains(chatId)) {
      chats = chats - chatId
      Success(chatId)
    } else {
      chatIdNotFoundFailure(chatId)
    }
  }

  def contains(chatId: ChatId) = {
    Success(chats.contains(chatId))
  }

  private def chatIdNotFoundFailure(chatId: ChatId) = {
    Failure(new IllegalArgumentException("No chat found"))
  }
}

