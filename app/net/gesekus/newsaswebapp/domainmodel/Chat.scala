package net.gesekus.newsaswebapp.domainmodel

case class ChatMessage(userId: UserId, message: String)
case class Chat(id: ChatId, owner: UserId,  messages: List[ChatMessage] = Nil)

