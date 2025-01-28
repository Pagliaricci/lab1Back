package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.UUID

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
    val timestamp: Long = System.currentTimeMillis()
)


data class ChatMessage(
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
