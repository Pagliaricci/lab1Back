package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class ObjRecord (
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val date : String,
    val objectiveName: String,
    val objectiveValue: Double,
    var currentValue: Double
)