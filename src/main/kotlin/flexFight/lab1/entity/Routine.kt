package flexFight.lab1.entity

import flexFight.lab1.repository.ExerciseRepository
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.util.*

data class CreateRoutine(
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,
    val exercises: List<RoutineExerciseDTO>  // DTO for exercise input
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

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Date = Date(),

    var isActive: Boolean = false,
    var rating: Double = 0.0,
) {
    constructor(createRoutine: CreateRoutine, exerciseRepository: ExerciseRepository) : this(
        name = createRoutine.name,
        duration = createRoutine.duration,
        intensity = createRoutine.intensity,
        price = createRoutine.price,
        creator = createRoutine.creator
    ) {
        // You can use this to populate RoutineExercise entities directly into the RoutineExercise table
        createRoutine.exercises.forEach { dto ->
            val exercise = exerciseRepository.findById(dto.exerciseId)
                .orElseThrow { IllegalArgumentException("Exercise with ID ${dto.exerciseId} not found") }
            val routineExercise = RoutineExercise(
                exercise = exercise,
                routine = this, // this refers to the current Routine entity
                sets = dto.sets.toString(),
                reps = dto.reps.toString(),
                day = dto.day
            )
            // Save RoutineExercise immediately if needed or manage it separately
        }
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
    val exerciseId: String,
    val sets: Int,
    val reps: Int,
    val day: Int
)

data class ActivateRoutine(
    val routineId: String,
    val userId: String
)

data class FullRoutine(
    val id: String ,
    val name: String,
    val duration: Int,
    val intensity: String,
    val price: String,
    val creator: String,
    val createdAt: Date,
    var isActive: Boolean,
    val exercises: List<FullRoutineExercise>
)


data class FullRoutineExercise(
    val name: String,
    val description: String,
    val category: String,
    val id: String,
    val routine: Routine,
    val sets: String,
    val reps: String,
    val day: Int
)

data class DeactivateRoutine(
    val routineId: String,
    val userId: String
)

data class GetExerciseHistory(
    val userId: String,
    val routineId: String,
    val routineExerciseId: String
)

data class RateRoutine(
    val routineId: String,
    val rating: Double
)