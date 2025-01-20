package flexFight.lab1.controller

import flexFight.lab1.entity.Chat
import flexFight.lab1.entity.Message
import flexFight.lab1.entity.MessageRequest
import flexFight.lab1.entity.User
import flexFight.lab1.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
class ChatsController(private val chatService: ChatService) {

    @GetMapping
    fun getChatsByUserId(@RequestParam userId: String): ResponseEntity<List<Chat>> {
        val chats = chatService.getChatsByUserId(userId)
        println("chats: $chats")
        return ResponseEntity.ok(chats)
    }

    @GetMapping("/trainers")
    fun getUserTrainers(@RequestParam userId: String): ResponseEntity<List<User>> {
        val trainers = chatService.getUserTrainers(userId)
        return ResponseEntity.ok(trainers)
    }

    @PostMapping("/start")
    fun startChat(@RequestParam userId: String, @RequestParam trainerId: String): ResponseEntity<String> {
        chatService.createChat(userId, trainerId)
        chatService.sendMessageToAll("New chat started between $userId and $trainerId")
        return ResponseEntity.ok("Chat started")
    }

    @GetMapping("/history")
    fun getChatHistory(@RequestParam chatId: String): ResponseEntity<List<Message>> {
        val messages = chatService.getChatHistory(chatId)
        return ResponseEntity.ok(messages)
    }


    @GetMapping("/{chatId}/messages")
    fun getChatMessages(@PathVariable chatId: String): ResponseEntity<List<Message>> {
        val messages = chatService.getChatHistory(chatId)
        return ResponseEntity.ok(messages)
    }

    @GetMapping("/{chatId}/receiver")
    fun getChatReceiver(@PathVariable chatId: String): ResponseEntity<User> {
        val receiver = chatService.getChatReceiver(chatId)
        return ResponseEntity.ok(receiver)
    }

    @PostMapping("/{chatId}/messages")
    fun sendMessage(
        @PathVariable chatId: String,
        @RequestBody messageRequest: MessageRequest
    ): ResponseEntity<Void> {
        chatService.sendMessage(chatId, messageRequest.userId, messageRequest.text)
        return ResponseEntity.ok().build()
    }
}