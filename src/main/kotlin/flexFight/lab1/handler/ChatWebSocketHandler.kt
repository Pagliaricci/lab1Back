package flexFight.lab1.handler

import flexFight.lab1.service.ChatService
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus

@Component
class ChatWebSocketHandler(private val chatService: ChatService) : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        // Process the payload and send a response if needed
        session.sendMessage(TextMessage("Message received: $payload"))
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        chatService.addSession(session)
        println("New WebSocket connection established")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        chatService.removeSession(session)
        println("WebSocket connection closed")
    }
}