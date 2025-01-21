package flexFight.lab1.entity

import jakarta.persistence.Id
import javax.print.attribute.standard.RequestingUserName


class ChatMessage(
    val content: String,
    val senderId: String,
    val type: MessageType
)

enum class MessageType {
    CHAT,
    JOIN,
    LEAVER
}


class ChatUser(
    @Id
    val username: String,
    var status : Status
)

enum class Status {
    ONLINE,
    OFFLINE
}