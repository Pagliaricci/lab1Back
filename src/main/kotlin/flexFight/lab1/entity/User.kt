package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

data class CreateUser (
    val username: String,
    val email : String,
    val password: String,
    val role: String,
    val weight: String,
    val height: String,
    val dateOfBirth: String,
    val gender: String,
)
@Entity
@Table(name = "app_user")
data class User (
    @Id val id: String = UUID.randomUUID().toString(),
    val username: String,
    val email : String,
    val password: String,
    val role: String,
    val weight: String,
    val height: String,
    val dateOfBirth: String,
    val gender: String,
) {
    constructor(createUser: CreateUser) : this(
        username = createUser.username,
        email = createUser.email,
        password = createUser.password,
        role = createUser.role,
        weight = createUser.weight,
        height = createUser.height,
        dateOfBirth = createUser.dateOfBirth,
        gender = createUser.gender,
        )
}

data class UpdateUser(
    val id: String,
    val weight: String,
    val height: String,
)

data class LoginUser(
    val username: String,
    val password: String,
)