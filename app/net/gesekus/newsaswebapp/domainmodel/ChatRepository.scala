package net.gesekus.newsaswebapp.domainmodel

import scala.util.{Try, Success, Failure}

/**
 * Created by tim on 15.03.2015.
 */

trait ChatRepository {
  def find(chatId: ChatId): Try[Chat]
  def store(chat: Chat) : Try[Chat]
  def remove(chatId: ChatId) : Try[ChatId]
  def contains(chatId: ChatId) : Try[Boolean]
}

