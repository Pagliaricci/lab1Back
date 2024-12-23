package flexFight.lab1.entity

import flexFight.lab1.repository.ExerciseRepository
import jakarta.persistence.*
import java.util.*

data class CreateWorkout(
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,
    val exercises: List<WorkoutExerciseDTO>
)


@Entity
data class Workout(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,

    @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL], orphanRemoval = true)
     val exercises: MutableList<WorkoutExercise> = mutableListOf(),

    var isActive: Boolean = false,
) {
    constructor(createWorkout: CreateWorkout, exerciseRepository: ExerciseRepository) : this(
        name = createWorkout.name,
        duration = createWorkout.duration,
        intensity = createWorkout.intensity,
        price = createWorkout.price,
        creator = createWorkout.creator
    ) {
        this.exercises.addAll(
            createWorkout.exercises.map { dto ->
                val exercise = exerciseRepository.findById(dto.exerciseId.toString())
                    .orElseThrow { IllegalArgumentException("Exercise with ID ${dto.exerciseId} not found") }
                WorkoutExercise(
                    exercise = exercise,
                    workout = this,
                    sets = dto.sets.toString(),
                    reps = dto.reps.toString(),
                    day = dto.day
                )
            }
        )
    }
}

@Entity
data class WorkoutExercise(
    @Id val id: String = UUID.randomUUID().toString(),
    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    val exercise: Exercise,
    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id")
    val workout: Workout,
    val sets: String,
    val reps: String,
    val day: Int,
)

data class WorkoutExerciseDTO(
    val exerciseId: Long,
    val sets: Int,
    val reps: Int,
    val day: Int
)
