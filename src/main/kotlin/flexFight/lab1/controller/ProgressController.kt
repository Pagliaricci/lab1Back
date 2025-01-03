package flexFight.lab1.controller

import flexFight.lab1.entity.*
import flexFight.lab1.service.HistoryExerciseService
import flexFight.lab1.service.ProgressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/progress")
class ProgressController(private val progressService: ProgressService,private val historyExerciseService: HistoryExerciseService
) {

    @PostMapping("/start-routine")
    fun startRoutine(@RequestBody startRoutine: StartRoutine): ResponseEntity<String> {
        val response = progressService.startRoutine(startRoutine)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/get-routine-progress")
    fun getRoutineProgress(@RequestBody getProgress: GetProgress): ResponseEntity<RoutineProgress?> {
        println("getRoutineProgress called with ${getProgress.userId} and ${getProgress.routineId}")
        val response = progressService.getRoutineProgress(getProgress)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/get-exercise-progress")
    fun getExerciseProgress(@RequestBody getExerciseProgress: GetExerciseProgress): ResponseEntity<List<FullExerciseProgress>?> {
        val response = progressService.getExerciseProgress(getExerciseProgress)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }


    @PostMapping("/complete-exercise")
    fun completeExercise(@RequestBody completeExercise: CompleteExercise): ResponseEntity<String> {
        val response = progressService.completeExercise(completeExercise)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/get-exercise-history")
    fun getExerciseHistory(@RequestBody getExerciseHistory: GetExerciseHistory): ResponseEntity<HistoryExercise> {
        val response = progressService.getExerciseHistory(getExerciseHistory)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/get-user-history")
    fun getUserHistory(@RequestHeader userId: String): ResponseEntity<List<FullHistoryExercise>> {
        val response = historyExerciseService.getUserHistory(userId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/update-progress-day")
    fun updateProgressDay(@RequestBody updateProgress: UpdateProgressDate): ResponseEntity<String> {
        val response = progressService.updateProgressDay(updateProgress)
        return ResponseEntity.ok(response)
    }

}