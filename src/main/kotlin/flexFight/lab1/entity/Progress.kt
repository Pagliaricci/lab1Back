package flexFight.lab1.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.Date
import java.util.UUID

@Entity
data class HistoryExercise(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val exerciseId: String,
    val routineId: String,
    val userId: String,
    val weight: Int,
    val reps: Int,
    val sets: Int,
    val date: Date = Date(),
    @ManyToOne
    @JoinColumn(name = "subscription_id")
    val subscription: Subscription? = null,
    )

@Entity
data class RoutineProgress(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val routineId: String,
    var day : Int,
    var amountOfExercisesDone: Int,
    var initiationDate: Date = Date(),
    var lastUpdated: Date = Date()
)

@Entity
data class ExerciseProgress(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val routineId: String,
    val routineExerciseId: String,
    var sets: Int,
    var reps: Int,
    val day: Int,
    var isDone: Boolean = false,
    val date: Date
)

data class AddRecord(
    val exerciseId: String,
    val routineId: String,
    val userId: String,
    val weight: Int,
    val reps: Int,
    val sets: Int,
    val subscription: Subscription? = null
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

data class CompleteExercise(
    val routineId: String,
    val userId: String,
    val routineExerciseId: String,
    val sets : Int,
    val reps : Int,
    val weight: Int,
    val day: Int,
)

data class GetExerciseProgress(
    val routineId: String,
    val userId: String
)

data class FullExerciseProgress(
    val routineExerciseId: String,
    val name : String,
    val category: String,
    val description: String,
    val userId: String,
    val routineId: String,
    val sets: Int,
    val reps: Int,
    val day: Int,
    val isDone: Boolean,
    val date: Date
)

data class FullHistoryExercise(
    val exerciseName : String,
    val routineName : String,
    val weight: Int,
    val reps: Int,
    val sets: Int,
    val date: Date
)


data class UpdateProgressDate(
    val userId: String,
    val routineId: String,
    val date: Date
)