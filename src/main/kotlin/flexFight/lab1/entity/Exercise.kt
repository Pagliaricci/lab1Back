package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class Exercise (
    @Id val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String
)

