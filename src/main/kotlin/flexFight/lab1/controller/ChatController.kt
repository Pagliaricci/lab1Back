package flexFight.lab1.controller


import flexFight.lab1.entity.ChatRoom
import flexFight.lab1.entity.MessageWithChatId
import flexFight.lab1.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
class ChatController(private val service: ChatService) {


    @GetMapping("/get/{userId}")
    fun getChats(@PathVariable userId: String): List<ChatRoomWithName> {
        return service.getChats(userId)
    }

    data class ChatRoomWithName(
        val id: String,
        val user1Name: String,
        val user2Name: String
    )
    @PostMapping("/create")
    fun createChat(@RequestBody chatRequest: ChatRequest): ResponseEntity<String> {
        val chat =  service.findOrCreateChat(chatRequest.user1Id, chatRequest.user2Id)
        println("EEEEEEU")
        println(chat.id.toString())
        println("a")
        return ResponseEntity.ok(chat.id.toString())
    }

    data class ChatRequest(val user1Id: String, val user2Id: String)

    @GetMapping("getMessages/{chatId}")
    fun getMessages(@PathVariable chatId: String): List<MessageWithChatId> {
        val messages = service.getChatMessages(chatId)
        return messages.map { MessageWithChatId(it.id.toString(),it.chatRoom.id.toString(), message = it.content, it.senderId,it.recipientId, timestamp = it.timestamp ) }
    }

    @GetMapping("getChatRoom/{chatId}")
    fun getChatRoom(@PathVariable chatId: String): ResponseEntity<ChatRoom> {
        println("chatId: $chatId")
        val chatRoom = service.getChatRoom(chatId)
        println("chatRoom: $chatRoom")
        return ResponseEntity.ok(chatRoom)
    }

    @GetMapping("/getRecipientId/{userId}/{chatId}")
    fun getRecipientId(@PathVariable userId: String, @PathVariable chatId: String): ResponseEntity<String> {
        val chatRoom = service.getChatRoom(chatId)
        val recipientId = if (userId == chatRoom.user1Id) chatRoom.user2Id else chatRoom.user1Id
        return ResponseEntity.ok(recipientId)
    }


}