package net.gesekus.newsaswebapp.domainmodel

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock._
import scala.util.Success
import org.specs2.mutable.Before

@RunWith(classOf[JUnitRunner])
class ChatServiceSpec extends Specification with Mockito {
  
  sequential
  
  val chatRepository = mock[ChatRepository]
  val config = mock[Config]
 
  val chat1Id = ChatId("Chat-1")
  val user1Id = UserId("User-1")
  val chat1 = Chat(chat1Id, user1Id)
  
  config.chatRepository returns chatRepository
  
  "ChatService" >> {
    "must return a valid chat for an empty repository" >> {
      chatRepository.contains(chat1Id) returns (Success(false))
      chatRepository.store(chat1) returns (Success(chat1))
      val chatTry = ChatService.open(user1Id,chat1Id ).run(config)
      chatTry.isSuccess && chatTry.get == chat1
    }
    
    "must return a failure for duplicate id" >> {
      chatRepository.contains(chat1Id) returns (Success(true))
      val chatTry = ChatService.open(user1Id, chat1Id).run(config)
      chatTry.isFailure
    }
  }
}