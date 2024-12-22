package flexFight.lab1.repository

import flexFight.lab1.entity.CreateUser
import flexFight.lab1.entity.UpdateUser
import flexFight.lab1.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,String> {
    fun findByUsernameAndPassword(username: String, password: String): User?
}