package flexFight.lab1.service

import flexFight.lab1.entity.CreateUser
import flexFight.lab1.entity.LoginUser
import flexFight.lab1.entity.UpdateUser
import flexFight.lab1.entity.User
import flexFight.lab1.repository.UserRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    private val logger: org.slf4j.Logger? = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(createUser: CreateUser): User {
        logger?.info("Creating user with username: ${createUser.username}")
        checkIfUserExists(createUser)
        checkCreateInputValid(createUser)
        val user = User(createUser)
        return userRepository.save(user)
    }

    private fun checkCreateInputValid(createUser: CreateUser) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (createUser.username.length < 3 ||
            createUser.password.length < 5 ||
            createUser.email.matches(emailRegex).not() ||
            createUser.role !in listOf("User", "Trainer") ||
            createUser.weight.toDoubleOrNull() == null || createUser.weight.toDouble() <= 0 ||
            createUser.height.toDoubleOrNull() == null || createUser.height.toDouble() <= 0 ||
            createUser.dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$".toRegex()).not() ||
            createUser.gender !in listOf("Male", "Female")
        ) {
            throw Exception("Invalid input")
        }
    }


    private fun checkIfUserExists(createUser: CreateUser) {
        if (userRepository.findByUsername(createUser.username) != null) {
            throw Exception("User already exists")
        }
    }

    fun updateUser(updateUser: UpdateUser): User {
        val user = getUser(updateUser.id)
        checkUpdateInput(updateUser)
        val updatedUser = user.copy(
            weight = updateUser.weight,
            height = updateUser.height
        )
        return userRepository.save(updatedUser)
    }

    private fun checkUpdateInput(updateUser: UpdateUser) {
        if (updateUser.weight.toLong() < 0 || updateUser.height.toLong() < 0) {
            throw Exception("Invalid input")
        }
    }


    fun deleteUser(id: String) {
        userRepository.deleteById(id)
    }

    fun getUser(id: String): User {
        return userRepository.findById(id)
            .orElseThrow { throw Exception("User not found") }
    }

    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    fun login(loginUser: LoginUser): User {
        return userRepository.findByUsernameAndPassword(loginUser.username, loginUser.password)
            ?: throw Exception("User not found")
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}