package net.gesekus.newsaswebapp.domainmodel

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock._
import scala.util.Success


@RunWith(classOf[JUnitRunner])
class ChatServiceSpec extends Specification with Mockito {
 "ChatService" >> {
    "must return a valid chat for an empty repository" >> {
      val chatRepository = mock[ChatRepository]
      val config = mock[Config]
      config.chatRepository returns chatRepository
      chatRepository.contains(ChatId("Chat-1")) returns false
      val chat = Chat(ChatId("Chat-1"), UserId("Tim"))
      chatRepository.store(chat) returns (Success(chat))
      val chatTry = ChatService.open(UserId("Tim"), ChatId("Chat-1")).run(config)
      chatTry.isSuccess
    }
    "must return a failure for duplicate id" >> {
   val chatRepository = mock[ChatRepository]
      val config = mock[Config]
      config.chatRepository returns chatRepository
      chatRepository.contains(ChatId("Chat-1")) returns true
      val chatTry = ChatService.open(UserId("Tim"), ChatId("Chat-1")).run(config)
      chatTry.isFailure
    }
  }

}