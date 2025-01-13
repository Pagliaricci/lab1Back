package flexFight.lab1.entity

import jakarta.persistence.*
import java.util.*


@Entity
data class Subscription(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val routineId: String,
    @ManyToOne
    @JoinColumn(name = "progress_id", referencedColumnName = "id")
    val progress: RoutineProgress,
    @OneToMany(mappedBy = "subscription", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val realizedExercises: List<ExerciseProgress> = listOf(),
    var isActive: Boolean = false
)


data class AddSubscriber(
    val userId: String,
    val routineId: String
)

data class SubscriberWithName(
    val id: String,
    val userId: String,
    val username: String,
    val routineId: String,
    val progress: RoutineProgress,
    val realizedExercises: List<ExerciseProgress>
)

data class SubscriberWithProgress(
    val userId: String,
    val username: String,
    val routineId: String,
    val progress: RoutineProgress,
    val exercisesProgress: List<ExerciseProgressWithDetails>
)

data class ExerciseProgressWithDetails(
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val date: Date
)
