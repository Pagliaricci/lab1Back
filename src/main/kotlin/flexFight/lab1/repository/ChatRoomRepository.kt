package flexFight.lab1.repository

import flexFight.lab1.chatRoom.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ChatRoomRepository: JpaRepository<ChatRoom,String> {
    fun findBySenderIdAndRecipientId(senderId: String, recipientId: String) : ChatRoom?
}