package flexFight.lab1.service

import flexFight.lab1.entity.CreateRoutine
import flexFight.lab1.entity.Routine
import flexFight.lab1.repository.ExerciseRepository
import flexFight.lab1.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository
) {

    @Transactional
    fun createRoutine(createRoutine: CreateRoutine): Routine {
        val routine = Routine(createRoutine, exerciseRepository)
        return routineRepository.save(routine)
    }
}
