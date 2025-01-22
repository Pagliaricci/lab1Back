package flexFight.lab1.repository

import flexFight.lab1.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, String> {
    fun findAllByChatId(chatId: String): List<ChatMessage>
}