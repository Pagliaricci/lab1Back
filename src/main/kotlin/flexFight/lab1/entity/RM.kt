package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class RM(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val exerciseId: String,
    var rm: Double,
    val date: Date
)

data class SetRM(
    val userId: String,
    val exerciseId: String,
    val reps: Int,
    val weight: Double,
    val date: Date
)