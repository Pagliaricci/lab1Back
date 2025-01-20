package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.*

@Entity
data class Chat(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    val sender: User,

    @ManyToOne
    val receiver: User,

    @OneToMany(mappedBy = "chat", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val messages: List<Message> = mutableListOf()
) {
    override fun toString(): String {
        return "Chat(id=$id, sender=${sender.id}, receiver=${receiver.id})"
    }
}
@Entity
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val chat: Chat,

    @ManyToOne
    val sender: User,

    val content: String,

    val timestamp: Date = Date()
) {
    override fun toString(): String {
        return "Message(id=$id, sender=${sender.id}, content=$content, timestamp=$timestamp)"
    }
}
data class MessageRequest(
    val userId: String,
    val text: String
)