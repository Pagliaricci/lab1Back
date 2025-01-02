package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.ExerciseRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import flexFight.lab1.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val routineExerciseRepository: RoutineExerciseRepository,
    private val progressService: ProgressService
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

    fun getRoutines(userID: String): List<Routine> {
        return routineRepository.findByCreatorOrderByCreatedAtDesc(userID)
    }
    fun activateRoutine(routineId: String) {
        val routine = routineRepository.findById(routineId)
            .orElseThrow { IllegalArgumentException("Routine with ID $routineId not found") }
        routine.isActive = true
        routineRepository.saveAndFlush(routine)
    }
    fun deactivateUserRoutines(userID: String) {
        val routines = routineRepository.findByCreatorOrderByCreatedAtDesc(userID)
        routines.forEach { routine ->
            routine.isActive = false
            routineRepository.saveAndFlush(routine)
        }
    }
    fun getActiveRoutine(userId: String): FullRoutine? {
        val routine = routineRepository.findByCreatorAndIsActive(userId, true)
        if (routine!= null){
            val routineExercises = routineExerciseRepository.findByRoutineId(routine.id)
            val fullRoutineExercises = routineExercises.map { routineExercise ->
                FullRoutineExercise(
                    name = routineExercise.exercise.name,
                    description = routineExercise.exercise.description,
                    category = routineExercise.exercise.category,
                    routine = routineExercise.routine,
                    id = routineExercise.id,
                    sets = routineExercise.sets,
                    reps = routineExercise.reps,
                    day = routineExercise.day
                )
            }
            val fullRoutine = FullRoutine(
                id = routine.id,
                name = routine.name,
                duration = routine.duration,
                intensity = routine.intensity,
                price = routine.price,
                creator = routine.creator,
                isActive = routine.isActive,
                createdAt = routine.createdAt,
                exercises = fullRoutineExercises
            )
            return fullRoutine
        }
        return null
    }
    @Transactional
    fun deleteRoutineById(id: String) {
        routineExerciseRepository.deleteAllByRoutineId(id)
        routineRepository.deleteById(id)
    }
    fun deactivateRoutine(deactivateRoutine: DeactivateRoutine) {
        progressService.deleteRoutineProgress(deactivateRoutine.userId, deactivateRoutine.routineId)
        progressService.deleteExerciseProgress(deactivateRoutine.userId, deactivateRoutine.routineId)
        val routine = routineRepository.findById(deactivateRoutine.routineId)
            .orElseThrow { IllegalArgumentException("Routine with ID ${deactivateRoutine.routineId} not found") }
        routine.isActive = false
        routineRepository.saveAndFlush(routine)
    }

}
