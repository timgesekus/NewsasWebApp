package websocketactors

import play.api.libs.json.Writes
import net.gesekus.newsaswebapp.views.ChatUpdated
import net.gesekus.newsaswebapp.domainmodel._
import net.gesekus.newsaswebapp.actors.AddChatMessage
import play.api.libs.json.JsPath
import play.api.libs.json.Json
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object JsonConverter {

  implicit val locationWrites = new Writes[ChatUpdated] {
    def writes(chatUpdated: ChatUpdated) = Json.obj(
      "event" -> "chatUpdated",
      "chatId" -> chatUpdated.chatId.id,
      "messages" -> chatUpdated.messages)
  }
  implicit val chatEventReads = ((JsPath \ "message").read[String]).map(NewMessage.apply _)
}