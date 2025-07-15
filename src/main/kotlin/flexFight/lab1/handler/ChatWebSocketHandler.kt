package flexFight.lab1.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import flexFight.lab1.service.ChatService
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(private val chatService: ChatService) : TextWebSocketHandler() {

    private val sessions = mutableMapOf<String, WebSocketSession>()
    private val mapper = jacksonObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.uri?.query // Obtener userID desde la URL
        if (userId != null) {
            sessions[userId] = session
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val payload = message.payload
            val received = mapper.readValue<IncomingMessage>(payload)
            val senderId = session.uri?.query ?: return

            // Guardar el mensaje en base de datos
            val savedMessage = chatService.saveMessage(received.chatId, senderId, received.content)

            val outgoing = OutgoingMessage(savedMessage.senderId, savedMessage.content)

            // Reenviar mensaje a todos los usuarios conectados
            val jsonMessage = mapper.writeValueAsString(outgoing)
            sessions.forEach { (_, userSession) ->
                if (userSession.isOpen) {
                    userSession.sendMessage(TextMessage(jsonMessage))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.values.remove(session)
    }

    data class IncomingMessage(val chatId: String, val content: String)
    data class OutgoingMessage(val senderId: String, val content: String)
}
