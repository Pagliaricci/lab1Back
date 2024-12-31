package flexFight.lab1.controller

import flexFight.lab1.entity.CreateUser
import flexFight.lab1.entity.LoginUser
import flexFight.lab1.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RequestMapping("/users")
@RestController
class UserController(private val service: UserService) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginUser: LoginUser, response: HttpServletResponse): Map<String, String> {
        try {
            val user = service.login(loginUser)

            val sessionToken = "valid-session-token-${user.username}"

            val cookie = Cookie("SESSION", sessionToken).apply {
                isHttpOnly = true
                path = "/"
                maxAge = 7 * 24 * 60 * 60 // 7 days
            }
            response.addCookie(cookie)

            return mapOf(
                "message" to "Login successful",
                "username" to user.username,
                "role" to user.role
            )
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        val cookie = Cookie("SESSION", "").apply {
            isHttpOnly = true
            path = "/"
            maxAge = 0
        }
        response.addCookie(cookie)

        return "Logout successful"
    }

    @GetMapping("/me")
    fun getLoggedInUser(request: HttpServletRequest): Map<String, String> {
        val sessionCookie = request.cookies?.firstOrNull { it.name == "SESSION" }
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No session found")

        if (!sessionCookie.value.startsWith("valid-session-token-")) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session")
        }

        val username = sessionCookie.value.removePrefix("valid-session-token-")

        val user = service.getUserByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")

        return mapOf(
            "userID" to user.id,
            "username" to user.username,
            "role" to user.role
        )
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