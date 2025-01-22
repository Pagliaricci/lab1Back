package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "chat_users")
class ChatUser(
    @Id
    val username: String,
    var status : Status
)

enum class Status {
    ONLINE,
    OFFLINE
}