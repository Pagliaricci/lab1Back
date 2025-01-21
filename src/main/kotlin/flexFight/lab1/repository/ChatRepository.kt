package flexFight.lab1.repository

import flexFight.lab1.entity.ChatUser
import flexFight.lab1.entity.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ChatRepository: JpaRepository<ChatUser,String> {
    fun findAllByStatus(online: Status): List<ChatUser>
}