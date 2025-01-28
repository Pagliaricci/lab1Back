package flexFight.lab1.repository

import flexFight.lab1.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRoomRepository : JpaRepository<ChatRoom, UUID> {
    fun findByUser1IdAndUser2Id(user1Id: String, user2Id: String): Optional<ChatRoom>
    fun findByUser1IdOrUser2Id(user1Id: String, user2Id: String): List<ChatRoom>

}
