package flexFight.lab1.controller

import flexFight.lab1.entity.CreateUser
import flexFight.lab1.entity.LoginUser
import flexFight.lab1.service.CourseService
import flexFight.lab1.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RequestMapping("/users")
@RestController
class UserController(private val service: UserService, private val courseService: CourseService) {

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
                maxAge = 24 * 60 * 60 // 1day p
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
            "role" to user.role,
            "email" to user.email,
            "gender" to user.gender,
            "dateOfBirth" to user.dateOfBirth
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
    @PostMapping("/get-height")
    fun getHeight(@RequestBody request: Map<String, String>): ResponseEntity<Double?> {
        val userId = request["userId"]

        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val height = service.getHeight(userId)
            ResponseEntity.ok(height)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
    @GetMapping
    fun getUsers(): List<Map<String, String>> {
        val users = service.getUsers().filter { it.role == "User" }.map {
            mapOf(
                "id" to it.id,
                "username" to it.username,
                "role" to it.role
            )
        }
        return users
    }

    @GetMapping("/subscribers/{userId}")
    fun getSubscribers(@PathVariable userId: String): List<Map<String, String>> {
        val subscribers = courseService.getAllMySubscribers(userId).map {
            mapOf(
                "id" to it.id,
                "username" to it.username,
                "role" to it.role
            )
        }
        return subscribers
    }

    @GetMapping("/trainers/{userId}")
    fun getTrainers(@PathVariable userId: String): List<Map<String, String>> {
        val trainers = courseService.getAllMyTrainers(userId).map {
            mapOf(
                "id" to it.id,
                "username" to it.username,
                "role" to it.role
            )
        }
        return trainers
    }




}