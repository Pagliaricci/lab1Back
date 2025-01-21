package flexFight.lab1.chatRoom

import flexFight.lab1.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService(private val chatRoomRepository: ChatRoomRepository) {

    fun getChatRoomId(senderId: String, recipientId: String, createNewRoomIfNotExists: Boolean): Optional<String> {
        val chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
        if (chatRoom != null) {
            return Optional.of(chatRoom.id)
        } else if (createNewRoomIfNotExists) {
            val chatId = createChat(senderId, recipientId)
            return Optional.of(chatId)
        }
        return Optional.empty()
    }

    fun createChat(senderId: String,recipientId: String):String{
        val chatId = String.format("%s_%s", senderId, recipientId)
        val senderRecipient = ChatRoom(UUID.randomUUID().toString(),chatId,senderId,recipientId)
        val recipientSender = ChatRoom(UUID.randomUUID().toString(),chatId,recipientId,senderId)
        chatRoomRepository.save(senderRecipient)
        chatRoomRepository.save(recipientSender)
        return chatId
    }
}