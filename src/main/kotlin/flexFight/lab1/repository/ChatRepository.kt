package flexFight.lab1.repository

import flexFight.lab1.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : JpaRepository<Chat, String> {

    @Query("SELECT c FROM Chat c WHERE c.sender.id = :userId OR c.receiver.id = :userId")
    fun findByUserId(@Param("userId") userId: String): List<Chat>
}