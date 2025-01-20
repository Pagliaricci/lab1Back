package flexFight.lab1.service

import flexFight.lab1.entity.Chat
import flexFight.lab1.entity.Message
import flexFight.lab1.entity.User
import flexFight.lab1.repository.ChatRepository
import flexFight.lab1.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userService: UserService,
    private val courseService: CourseService,
    private val routineService: RoutineService,
) {
    private val sessions = mutableListOf<WebSocketSession>()

    fun getChatsByUserId(userId: String): List<Chat> {
        return chatRepository.findByUserId(userId)
    }

    fun createChat(userId: String, trainerId: String): Chat {
        val user = userService.getUser(userId)
        val trainer = userService.getUser(trainerId)
        val chat = Chat(sender = user, receiver = trainer)
        return chatRepository.save(chat)
    }

    fun getUserTrainers(userId: String): List<User> {
        val subs = courseService.getUserSubscriptions(userId)
        return subs.map { subscription -> userService.getUser(routineService.getRoutine(subscription.routineId).creator) }
    }

    fun sendMessage(chatId: String, senderId: String, content: String) {
        val chat = chatRepository.findById(chatId).orElseThrow { RuntimeException("Chat not found") }
        val sender = userService.getUser(senderId)
        val message = Message(content = content, chat = chat, sender = sender)
        messageRepository.save(message)
        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(TextMessage("New message: $content"))
            }
        }
    }

    fun sendMessageToAll(message: String) {
        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(TextMessage(message))
            }
        }
    }

    fun addSession(session: WebSocketSession) {
        sessions.add(session)
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    fun getChatHistory(chatId: String): List<Message> {
        val chat = chatRepository.findById(chatId).orElseThrow { RuntimeException("Chat not found") }
        return chat.messages
    }

    fun getChatReceiver(chatId: String): User {
        val chat = chatRepository.findById(chatId).orElseThrow { RuntimeException("Chat not found") }
        return chat.receiver
    }
}