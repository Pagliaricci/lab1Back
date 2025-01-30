package flexFight.lab1.service

import flexFight.lab1.controller.ChatController
import flexFight.lab1.entity.ChatRoom
import flexFight.lab1.entity.Message
import flexFight.lab1.repository.ChatRoomRepository
import flexFight.lab1.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService(
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository,
    private val userService: UserService
) {
    fun findOrCreateChat(user1Id: String, user2Id: String): ChatRoom {
        return chatRoomRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
            .orElseGet { chatRoomRepository.save(ChatRoom(user1Id = user1Id, user2Id = user2Id)) }
    }

    fun getChats(userId: String): List<ChatController.ChatRoomWithName> {
        val chatRooms = chatRoomRepository.findByUser1IdOrUser2Id(userId, userId)
        return chatRooms.map { ChatController.ChatRoomWithName(it.id.toString(), userService.getUser(it.user1Id).username,userService.getUser(it.user2Id).username) }
    }

    fun getChatMessages(chatId: String): List<Message> {
        val chatRoom = chatRoomRepository.findById(UUID.fromString(chatId)).get()
        return messageRepository.findByChatRoomId(chatRoom.id!!)
    }

    fun saveMessage(chatId: String,  senderId: String, text: String): Message {
        val chatRoom = chatRoomRepository.findById(UUID.fromString(chatId)).get()
        val recipientId = if (senderId == chatRoom.user1Id) chatRoom.user2Id else chatRoom.user1Id
        val message = messageRepository.save(Message(chatRoom = chatRoom, senderId = senderId, recipientId = recipientId, content = text))
        return message
    }

    fun getChatRoom(chatId: String): ChatRoom {
        return chatRoomRepository.findById(UUID.fromString(chatId)).get()
    }
}
