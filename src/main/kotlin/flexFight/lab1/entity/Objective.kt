package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id


@Entity
data class Objective(
    @Id
    val id: String,
    val userId: String,
    var rm: Double,
    var weight: Int,
    var imc: Double,
    val date: String
)