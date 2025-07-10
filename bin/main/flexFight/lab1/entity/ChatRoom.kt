package flexFight.lab1.entity

import java.util.UUID

import jakarta.persistence.*

@Entity
@Table(name = "chat_rooms")
data class ChatRoom(
    @Id @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val user1Id: String,

    @Column(nullable = false)
    val user2Id: String
)
