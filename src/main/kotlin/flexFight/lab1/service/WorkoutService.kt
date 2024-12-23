package flexFight.lab1.service

import flexFight.lab1.entity.CreateWorkout
import flexFight.lab1.entity.Workout
import flexFight.lab1.repository.ExerciseRepository
import flexFight.lab1.repository.WorkoutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) {

    @Transactional
    fun createWorkout(createWorkout: CreateWorkout): Workout {
        val workout = Workout(createWorkout, exerciseRepository)
        return workoutRepository.save(workout)
    }
}
