package flexFight.lab1.repository

import flexFight.lab1.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MessageRepository : JpaRepository<Message, UUID> {
    fun findByChatRoomId(chatId: UUID): List<Message>
}
