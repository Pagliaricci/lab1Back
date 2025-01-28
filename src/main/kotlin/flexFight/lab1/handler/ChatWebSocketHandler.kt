package flexFight.lab1.handler

import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

class ChatWebSocketHandler : TextWebSocketHandler() {
    private val sessions = mutableMapOf<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.uri?.query // Obtener userID desde la URL
        if (userId != null) {
            sessions[userId] = session
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload.split(":") // Ejemplo de mensaje: "destinatarioID:Hola!"
        val recipientId = payload[0]
        val text = payload[1]

        sessions[recipientId]?.sendMessage(TextMessage(text))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.values.remove(session)
    }
}
