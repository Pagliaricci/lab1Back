package flexFight.lab1.service

import flexFight.lab1.entity.GetProgress
import flexFight.lab1.entity.RoutineProgress
import flexFight.lab1.entity.StartRoutine
import flexFight.lab1.entity.UpdateProgress
import flexFight.lab1.repository.ProgressRepository
import org.springframework.stereotype.Service

@Service
class ProgressService(private val progressRepository: ProgressRepository) {

    fun startRoutine(startRoutine: StartRoutine): String {
        try {
            val progress = RoutineProgress(
                userId = startRoutine.userId,
                routineId = startRoutine.routineId,
                day = startRoutine.day,
                amountOfExercisesDone = startRoutine.amountOfExercisesDone
            )
            progressRepository.saveAndFlush(progress)
            return "Routine started successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun getRoutineProgress(getProgress: GetProgress): RoutineProgress? {
        return try {
            progressRepository.findByUserIdAndRoutineId(getProgress.userId, getProgress.routineId)
        } catch (e: Exception) {
            null
        }
    }

    fun updateRoutineProgress(routineProgress: UpdateProgress): String {
        try {
            val progress = progressRepository.findByUserIdAndRoutineId(routineProgress.userId, routineProgress.routineId)
            progress?.day = routineProgress.day
            progress?.amountOfExercisesDone = routineProgress.amountOfExercisesDone
            progressRepository.saveAndFlush(progress!!)
            return "Routine progress updated successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }


}