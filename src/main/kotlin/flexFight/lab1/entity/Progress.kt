package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.Date
import java.util.UUID

@Entity
data class HistoryExercise(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val exerciseId: String,
    val routineId: String,
    val userId: String,
    val weight: String,
    val reps: String,
    val sets: String,
    val date: Date = Date()
)

@Entity
data class RoutineProgress(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val routineId: String,
    var day : Int,
    var amountOfExercisesDone: Int
)

data class AddRecord(
    val exerciseId: String,
    val routineId: String,
    val userId: String,
    val weight: String,
    val reps: String,
    val sets: String
)

data class StartRoutine(
    val routineId: String,
    val userId: String,
    val day: Int = 1,
    val amountOfExercisesDone: Int = 0
)

data class GetProgress(
    val routineId: String,
    val userId: String
)

data class UpdateProgress(
    val userId: String,
    val routineId: String,
    var day : Int,
    var amountOfExercisesDone: Int
)