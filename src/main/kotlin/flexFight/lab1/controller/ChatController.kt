package flexFight.lab1.controller


import flexFight.lab1.entity.ChatMessage
import flexFight.lab1.entity.ChatNotification
import flexFight.lab1.service.ChatMessageService
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ChatController(private val chatMessageService: ChatMessageService,
                     private val simpMessagingTemplate: SimpMessagingTemplate
) {

    @GetMapping("/messages/{senderId}/{recipientId}")
    fun findChatMessages(@PathVariable senderId: String, @PathVariable recipientId: String): ResponseEntity<List<ChatMessage>> {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId))
    }


    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessage: ChatMessage) {
        val savedMessage = chatMessageService.saveChatMessage(chatMessage)
        simpMessagingTemplate.convertAndSendToUser(
            chatMessage.recipientId, "/queue/messages",
            ChatNotification(
                savedMessage.id,
                savedMessage.senderId,
                savedMessage.recipientId,
                savedMessage. content,
            )
        )
    }
}