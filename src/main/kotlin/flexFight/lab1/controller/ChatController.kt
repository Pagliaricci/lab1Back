package flexFight.lab1.controller


import flexFight.lab1.entity.ChatRoom
import flexFight.lab1.service.ChatService
import flexFight.lab1.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/chats")
class ChatController(private val service: ChatService, private val userService: UserService) {


    @GetMapping("/get/{userId}")
    fun getChats(@PathVariable userId: String): List<ChatRoom> {
        return service.getChats(userId)
    }

    @PostMapping("/create")
    fun createChat(@RequestBody user1Id: String, @RequestBody user2Id: String): ChatRoom {
        return service.findOrCreateChat(user1Id, user2Id)
    }

    @GetMapping("getMessages/{userId}&{recipientId}")
    fun getMessages(@PathVariable userId: String, @PathVariable recipientId: String): List<ChatRoom> {
        return service.getChatMessages(userId, recipientId)
    }


}