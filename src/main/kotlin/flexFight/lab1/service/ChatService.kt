package flexFight.lab1.service

import flexFight.lab1.entity.ChatRoom
import flexFight.lab1.entity.Message
import flexFight.lab1.repository.ChatRoomRepository
import flexFight.lab1.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository
) {
    fun findOrCreateChat(user1Id: String, user2Id: String): ChatRoom {
        return chatRoomRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
            .orElseGet { chatRoomRepository.save(ChatRoom(user1Id = user1Id, user2Id = user2Id)) }
    }

    fun getChats(userId: String): List<ChatRoom> {
        return chatRoomRepository.findByUser1IdOrUser2Id(userId, userId)
    }

    fun getChatMessages(userId: String, recipientId: String): List<Message> {
        val chatRoom = chatRoomRepository.findByUser1IdAndUser2Id(userId, recipientId)
            .orElseGet { chatRoomRepository.findByUser1IdAndUser2Id(recipientId, userId).orElseThrow { IllegalArgumentException("Chat not found") } }
        return messageRepository.findByChatRoomId(chatRoom.id!!)
    }

}
