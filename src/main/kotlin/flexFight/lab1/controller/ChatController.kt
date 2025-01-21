package flexFight.lab1.controller

import flexFight.lab1.entity.ChatUser
import flexFight.lab1.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.*

@RestController
class ChatController(private val userService: UserService) {


    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    fun addUser( @Payload user: ChatUser): ChatUser{
        userService.saveUser(user)
        return user
    }


    @MessageMapping("/user.disconnect")
    @SendTo("/user/topic")
    fun disconnect(@Payload user: ChatUser): ChatUser{
        userService.disconnect(user)
        return user
    }

    @GetMapping("/users")
    fun findConnectedUsers(): ResponseEntity<List<ChatUser>>{
        return ResponseEntity.ok(userService.findConnectedUsers())
    }


}
