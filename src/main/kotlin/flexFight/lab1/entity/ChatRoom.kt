package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class ChatRoom (
    @Id
    val id: String = UUID.randomUUID().toString(),
    val chatId: String,
    val senderId : String,
    val recipientId: String

)