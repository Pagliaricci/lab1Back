package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.ExerciseProgressRepository
import flexFight.lab1.repository.ProgressRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Service
class ProgressService(private val progressRepository: ProgressRepository, private val exerciseProgressRepository: ExerciseProgressRepository, private val routineExerciseRepository: RoutineExerciseRepository, private val historyExerciseService: HistoryExerciseService) {

    fun startRoutine(startRoutine: StartRoutine): String {
        try {
            saveRoutineProgress(startRoutine)
            saveExerciseProgress(startRoutine)
            return "Routine started successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun saveRoutineProgress(startRoutine: StartRoutine) {
        val progress = progressRepository.findByUserIdAndRoutineId(startRoutine.userId, startRoutine.routineId)
        if (progress != null) {
            progress.day = startRoutine.day
            progress.amountOfExercisesDone = startRoutine.amountOfExercisesDone
            progressRepository.saveAndFlush(progress)
            return
        }
        val newProgress = RoutineProgress(
            userId = startRoutine.userId,
            routineId = startRoutine.routineId,
            day = startRoutine.day,
            amountOfExercisesDone = startRoutine.amountOfExercisesDone
        )
        progressRepository.saveAndFlush(newProgress)
    }

    private fun saveExerciseProgress(startRoutine: StartRoutine) {
        val exercises = routineExerciseRepository.findByRoutineId(startRoutine.routineId)
        exercises.forEach {
            val exerciseProgress = ExerciseProgress(
                userId = startRoutine.userId,
                routineId = startRoutine.routineId,
                routineExerciseId = it.id,
                sets = it.sets.toInt(),
                reps = it.reps.toInt(),
                day = it.day,
                date = getCorrespondingDate(it.day)
            )
            exerciseProgressRepository.saveAndFlush(exerciseProgress)
        }
    }

    private fun getCorrespondingDate(day: Int): Date {
        val currentDate = LocalDate.now()
        val newDate = currentDate.plusDays(day.toLong())
        return Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    fun getRoutineProgress(getProgress: GetProgress): RoutineProgress? {
         try {
            val progress = progressRepository.findByUserIdAndRoutineId(getProgress.userId, getProgress.routineId)
             if (progress != null) {
                 if (progress.day ==0){
                     return null
                 }
                 return progress
             }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    fun updateRoutineProgress(routineProgress: UpdateProgress): String {
        try {
            val progress = progressRepository.findByUserIdAndRoutineId(routineProgress.userId, routineProgress.routineId)
            progress?.day = routineProgress.day
            progress?.amountOfExercisesDone = progress?.amountOfExercisesDone?.plus(1)!!
            progressRepository.saveAndFlush(progress)
            return "Routine progress updated successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun completeExercise(completeExercise: CompleteExercise): String {
        try {
            addRecord(completeExercise)
            updateRoutineProgress(UpdateProgress(completeExercise.userId, completeExercise.routineId, completeExercise.day, 1))
            updateExerciseProgress(completeExercise)
            return "Exercise completed successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun updateExerciseProgress(completeExercise: CompleteExercise) {
        val exerciseProgress = exerciseProgressRepository.findByUserIdAndRoutineIdAndRoutineExerciseIdOrderByDateDesc(
            completeExercise.userId,
            completeExercise.routineId,
            completeExercise.routineExerciseId
        )
        exerciseProgress?.isDone = true
        exerciseProgressRepository.saveAndFlush(exerciseProgress!!)
    }

    private fun addRecord(completeExercise: CompleteExercise) {
        val historyRecord = AddRecord(
            exerciseId = completeExercise.routineExerciseId,
            userId = completeExercise.userId,
            weight = completeExercise.weight,
            reps = completeExercise.reps,
            sets = completeExercise.sets,
            routineId = completeExercise.routineId
        )
        historyExerciseService.addRecord(historyRecord)
    }

    fun getExerciseProgress(getExerciseProgress: GetExerciseProgress): List<FullExerciseProgress>? {
        return try {
            val exercises = exerciseProgressRepository.findByUserIdAndRoutineId(getExerciseProgress.userId, getExerciseProgress.routineId)
            val fullExerciseProgress = mutableListOf<FullExerciseProgress>()
            exercises.forEach {
                val exercise = routineExerciseRepository.findById(it.routineExerciseId).get()
                fullExerciseProgress.add(
                    FullExerciseProgress(
                        routineExerciseId = it.routineExerciseId,
                        name = exercise.exercise.name,
                        category = exercise.exercise.category,
                        description = exercise.exercise.description,
                        userId = it.userId,
                        routineId = it.routineId,
                        sets = it.sets,
                        reps = it.reps,
                        day = it.day,
                        isDone = it.isDone,
                        date = it.date
                    )
                )
            }
            fullExerciseProgress
        } catch (e: Exception) {
            null
        }
    }

    fun deleteRoutineProgress(userId: String, routineId: String): String {
        try {
            println("Deleting routine progress for user $userId and routine $routineId")
            progressRepository.deleteByUserIdAndRoutineId(userId, routineId)
            return "Routine progress deleted successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun deleteExerciseProgress(userId: String, routineId: String): String {
        try {
            exerciseProgressRepository.deleteByUserIdAndRoutineId(userId, routineId)
            return "Exercise progress deleted successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

        fun getExerciseHistory(getExerciseHistory: GetExerciseHistory): HistoryExercise {
            return try {
                historyExerciseService.getExerciseHistory(getExerciseHistory)
            } catch (e: Exception) {
                throw Exception("Error: ${e.message}")
            }
        }

fun updateProgressDay(updateProgressDate: UpdateProgressDate): RoutineProgress? {
    try {
        val progress = progressRepository.findByUserIdAndRoutineId(updateProgressDate.userId, updateProgressDate.routineId)
        if (progress != null) {
            val daysDifference = java.time.temporal.ChronoUnit.DAYS.between(progress.lastUpdated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), updateProgressDate.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            progress.day += daysDifference.toInt()
            progress.lastUpdated = updateProgressDate.date
            progressRepository.saveAndFlush(progress)
            return progress
        }
        else {
            return null
        }
    } catch (e: Exception) {
        return null
    }
    }

    fun setProgressToCero(userId: String, routineId: String): String {
        try {
            val routineProgress = progressRepository.findByUserIdAndRoutineId(userId, routineId) ?: return "Progress not found"
            routineProgress.day = 0
            routineProgress.amountOfExercisesDone = 0
            routineProgress.initiationDate = Date()
            routineProgress.lastUpdated = Date()
            progressRepository.saveAndFlush(routineProgress)
            return "Progress set to cero successfully"

        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun setExerciseProgressToCero(userId: String, routineId: String): String {
        try {
            val exercises = exerciseProgressRepository.findByUserIdAndRoutineId(userId, routineId)
            exercises.forEach {
                it.isDone = false
                exerciseProgressRepository.saveAndFlush(it)
            }
            return "Exercise progress set to cero successfully"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

}