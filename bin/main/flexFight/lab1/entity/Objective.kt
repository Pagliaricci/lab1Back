package flexFight.lab1.entity

import jakarta.persistence.Entity


data class Objective(
    val objective: Double,
    val isHigher: Boolean
)
