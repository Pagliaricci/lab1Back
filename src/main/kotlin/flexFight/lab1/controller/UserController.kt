package flexFight.lab1.controller

import flexFight.lab1.entity.CreateUser
import flexFight.lab1.entity.LoginUser
import flexFight.lab1.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(private val service: UserService) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginUser: LoginUser): String {
        try {
            service.login(loginUser)
            return "Login successful"
        }catch (e: Exception) {
            return e.message.toString()
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody createUser: CreateUser): String {
       try {
           service.createUser(createUser)
           return "User created successfully"
       }
         catch (e: Exception) {
              return e.message.toString()
         }
    }

}