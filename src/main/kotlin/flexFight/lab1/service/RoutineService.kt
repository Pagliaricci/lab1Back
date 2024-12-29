package flexFight.lab1.service

import flexFight.lab1.entity.CreateRoutine
import flexFight.lab1.entity.Routine
import flexFight.lab1.entity.RoutineExercise
import flexFight.lab1.repository.ExerciseRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import flexFight.lab1.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val routineExerciseRepository: RoutineExerciseRepository // New repository for RoutineExercise
) {

    @Transactional
    fun createRoutine(createRoutine: CreateRoutine): Routine {
        // Create the Routine entity from the CreateRoutine DTO
        val routine = Routine(createRoutine, exerciseRepository)

        // Save the Routine first
        routineRepository.save(routine)

        createRoutine.exercises.forEach { dto ->
            val exercise = exerciseRepository.findById(dto.exerciseId)
                .orElseThrow { IllegalArgumentException("Exercise with ID ${dto.exerciseId} not found") }

            val routineExercise = RoutineExercise(
                exercise = exercise,
                routine = routine,
                sets = dto.sets.toString(),
                reps = dto.reps.toString(),
                day = dto.day
            )

            routineExerciseRepository.save(routineExercise)
        }

        return routine
    }

    fun getRoutineExercises(routineId: String): List<RoutineExercise> {
        return routineExerciseRepository.findByRoutineId(routineId)
    }
}
