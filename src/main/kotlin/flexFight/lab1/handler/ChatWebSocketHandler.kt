package flexFight.lab1.handler

import flexFight.lab1.entity.Message
import flexFight.lab1.service.ChatService
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.stereotype.Component

@Component
class ChatWebSocketHandler(private val chatService: ChatService) : TextWebSocketHandler() {
    private val sessions = mutableMapOf<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.uri?.query // Obtener userID desde la URL
        if (userId != null) {
            sessions[userId] = session
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload.split(":") // Ejemplo de mensaje: "chatId:contenido"
        if (payload.size < 2) return

        val chatId = payload[0]
        val text = payload[1]
        val senderId = session.uri?.query ?: return  // Obtener senderId desde la URL


        // 1️⃣ Guardar el mensaje en la base de datos
        val savedMessage = chatService.saveMessage(chatId, senderId, text)

        // 2️⃣ Reenviar mensaje al destinatario
        sessions.forEach { (userId, userSession) ->
            if (userSession.isOpen) {
                userSession.sendMessage(TextMessage("${savedMessage.senderId}:${savedMessage.content}"))
            }
        }

    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.values.remove(session)
    }
}
