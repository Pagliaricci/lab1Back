package flexFight.lab1.repository

import flexFight.lab1.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, String> {
}