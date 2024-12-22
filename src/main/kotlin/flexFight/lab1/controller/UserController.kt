package flexFight.lab1.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }

}