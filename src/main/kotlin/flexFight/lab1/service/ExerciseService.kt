package flexFight.lab1.service

import flexFight.lab1.entity.Exercise
import flexFight.lab1.repository.ExerciseRepository
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository
) {

    fun createExercise(exercise: Exercise): Exercise {
        return exerciseRepository.save(exercise)
    }
}
