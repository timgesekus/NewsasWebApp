package net.gesekus.newsaswebapp.domainmodel

import scala.util.{ Try, Success, Failure }
import scalaz._, Scalaz._
import scalaz.contrib.std.utilTry._

/**
 * Created by tim on 15.03.2015.
 */
trait Chats {

  import scalaz.Reader

  def validateChatId(chatId: ChatId): ReaderT[Try, Config, ChatId] = {
    Kleisli.kleisli(config => {

      config.chatRepository.contains(chatId) match {
        case Success(isUsed) => if (!isUsed) {
          Success(chatId)
        } else {
          Failure(new IllegalArgumentException("ChatId allready in use"))
        }
        case Failure(e) => Failure(e)
      }
    })
  }

  def find(chatId: ChatId): ReaderT[Try, Config, Chat] = {
    Kleisli.kleisli(config => {
      config.chatRepository.find(chatId)
    })
  }

  def store(chat: Chat): ReaderT[Try, Config, Chat] =
    Kleisli.kleisli(config => {
      config.chatRepository.store(chat)
    })

  def remove(chatId: ChatId): ReaderT[Try, Config, ChatId] =
    Kleisli.kleisli(config => {
      config.chatRepository.remove(chatId)
    })
}
