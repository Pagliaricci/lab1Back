package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.Date
import java.util.UUID
import javax.management.monitor.StringMonitor

@Entity
@Table(name = "messages")
data class Message(
    @Id @GeneratedValue
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    val chatRoom: ChatRoom,

    @Column(nullable = false)
    val senderId: String,

    @Column(nullable = false)
    val recipientId: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    val timestamp: String = System.currentTimeMillis().toString()
)

data class MessageWithChatId(
    val id: String,
    val chatId: String,
    val message: String,
    val senderId: String,
    val recipientId: String,
    val timestamp: String,
)
data class ChatMessage(
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
