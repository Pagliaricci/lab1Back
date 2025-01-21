package flexFight.lab1.chatRoom

import jakarta.persistence.Id

data class ChatRoom (
    @Id
    val id: String,
    val chatId: String,
    val senderId : String,
    val recipientId: String

)