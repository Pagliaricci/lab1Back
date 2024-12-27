package flexFight.lab1.entity

import flexFight.lab1.repository.ExerciseRepository
import jakarta.persistence.*
import java.util.*

data class CreateRoutine(
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,
    val exercises: List<RoutineExerciseDTO>
)


@Entity
data class Routine(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,

    @OneToMany(mappedBy = "routine", cascade = [CascadeType.ALL], orphanRemoval = true)
     val exercises: MutableList<RoutineExercise> = mutableListOf(),

    var isActive: Boolean = false,
) {
    constructor(createRoutine: CreateRoutine, exerciseRepository: ExerciseRepository) : this(
        name = createRoutine.name,
        duration = createRoutine.duration,
        intensity = createRoutine.intensity,
        price = createRoutine.price,
        creator = createRoutine.creator
    ) {
        this.exercises.addAll(
            createRoutine.exercises.map { dto ->
                val exercise = exerciseRepository.findById(dto.exerciseId.toString())
                    .orElseThrow { IllegalArgumentException("Exercise with ID ${dto.exerciseId} not found") }
                RoutineExercise(
                    exercise = exercise,
                    routine = this,
                    sets = dto.sets.toString(),
                    reps = dto.reps.toString(),
                    day = dto.day
                )
            }
        )
    }
}

@Entity
data class RoutineExercise(
    @Id val id: String = UUID.randomUUID().toString(),
    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    val exercise: Exercise,
    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "id")
    val routine: Routine,
    val sets: String,
    val reps: String,
    val day: Int,
)

data class RoutineExerciseDTO(
    val exerciseId: Long,
    val sets: Int,
    val reps: Int,
    val day: Int
)
