package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.*

@Entity
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val senderId: String,
    val recipientId: String,
    var chatId: String,
    val date: Date
)