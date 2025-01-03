package flexFight.lab1.service

import flexFight.lab1.entity.AddRecord
import flexFight.lab1.entity.FullHistoryExercise
import flexFight.lab1.entity.GetExerciseHistory
import flexFight.lab1.entity.HistoryExercise
import flexFight.lab1.repository.HistoryExerciseRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import flexFight.lab1.repository.RoutineRepository
import org.springframework.stereotype.Service

@Service
class HistoryExerciseService(
    private val historyExerciseRepository: HistoryExerciseRepository,
    private val routineRepository: RoutineRepository,
    private val routineExerciseRepository: RoutineExerciseRepository
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
    fun getUserHistory(userId: String): List<FullHistoryExercise> {
        return try {
            val exercises = historyExerciseRepository.findByUserId(userId)
            exercises.map {
                FullHistoryExercise(
                    exerciseName = getExerciseName(it.exerciseId),
                    weight = it.weight,
                    reps = it.reps,
                    sets = it.sets,
                    routineName = getRoutineName(it.routineId),
                    date = it.date
                )
            }
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    private fun getExerciseName(exerciseId: String): String {
        val routineExercise = routineExerciseRepository.findById(exerciseId).get()
    return routineExercise.exercise.name
    }
    private fun getRoutineName(routineId: String): String {
        return routineRepository.findById(routineId).get().name
    }
}
