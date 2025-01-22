package flexFight.lab1.service

import flexFight.lab1.repository.ChatMessageRepository
import flexFight.lab1.entity.ChatMessage
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomService: ChatRoomService
) {

    fun findChatMessages(senderId: String, recipientId: String): List<ChatMessage> {
        val chatRoomId = chatRoomService.getChatRoomId(senderId, recipientId, false)
            .orElseThrow { RuntimeException("Chat room not found") }
        return chatMessageRepository.findAllByChatId(chatRoomId)
    }

    fun saveChatMessage(chatMessage: ChatMessage): ChatMessage {
        val chatRoomId = chatRoomService.getChatRoomId(chatMessage.senderId, chatMessage.recipientId, true)
            .orElseThrow { RuntimeException("Chat room not found") }
        chatMessage.chatId = chatRoomId
        return chatMessageRepository.save(chatMessage)
    }
}