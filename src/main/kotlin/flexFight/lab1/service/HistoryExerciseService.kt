package flexFight.lab1.service

import flexFight.lab1.entity.AddRecord
import flexFight.lab1.entity.GetExerciseHistory
import flexFight.lab1.entity.HistoryExercise
import flexFight.lab1.repository.HistoryExerciseRepository
import org.springframework.stereotype.Service

@Service
class HistoryExerciseService(
    private val historyExerciseRepository: HistoryExerciseRepository
) {
    fun addRecord(addRecord: AddRecord): String {
        try {
            val historyExercise = HistoryExercise(
                exerciseId = addRecord.exerciseId,
                userId = addRecord.userId,
                weight = addRecord.weight,
                reps = addRecord.reps,
                sets = addRecord.sets,
                routineId = addRecord.routineId
            )
            historyExerciseRepository.saveAndFlush(historyExercise)
            return "Record added successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun getExerciseHistory(getExerciseHistory: GetExerciseHistory): HistoryExercise {
        return try {
            historyExerciseRepository.findByUserIdAndExerciseIdAndRoutineId(getExerciseHistory.userId, getExerciseHistory.routineExerciseId, getExerciseHistory.routineId)!!
        }
        catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }
}