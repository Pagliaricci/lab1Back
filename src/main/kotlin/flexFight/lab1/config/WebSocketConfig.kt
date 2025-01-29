package flexFight.lab1.config

import flexFight.lab1.handler.ChatWebSocketHandler
import flexFight.lab1.service.ChatService
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
class WebSocketConfig(private val  chatService: ChatService) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ChatWebSocketHandler(chatService), "/ws").setAllowedOrigins("*")
    }
}
