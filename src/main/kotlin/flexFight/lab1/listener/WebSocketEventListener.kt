package flexFight.lab1.listener

import flexFight.lab1.entity.ChatMessage
import flexFight.lab1.entity.MessageType
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.slf4j.LoggerFactory

@Component
class WebSocketEventListener(private val messageTemplate: SimpMessageSendingOperations) {
    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketDisconnectListener(disconnect: SessionDisconnectEvent) {
        val headerAccessor: StompHeaderAccessor = StompHeaderAccessor.wrap(disconnect.message)
        val username: String? = headerAccessor.sessionAttributes?.get("username") as? String
        if (username != null) {
            logger.info("User Disconnected: $username")
            val chatMessage = ChatMessage("", username, MessageType.LEAVER)
            messageTemplate.convertAndSend("/topic/public", chatMessage)
        }
    }
}