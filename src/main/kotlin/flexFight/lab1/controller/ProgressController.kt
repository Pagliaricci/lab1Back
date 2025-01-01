package flexFight.lab1.controller

import flexFight.lab1.entity.GetProgress
import flexFight.lab1.entity.RoutineProgress
import flexFight.lab1.entity.StartRoutine
import flexFight.lab1.entity.UpdateProgress
import flexFight.lab1.service.ProgressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/progress")
class ProgressController(private val progressService: ProgressService) {

    @PostMapping("/start-routine")
    fun startRoutine(@RequestBody startRoutine: StartRoutine): ResponseEntity<String> {
        val response = progressService.startRoutine(startRoutine)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/get-routine-progress")
    fun getRoutineProgress(@RequestBody getProgress: GetProgress): ResponseEntity<RoutineProgress?> {
        val response = progressService.getRoutineProgress(getProgress)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/update-routine-progress")
    fun updateRoutineProgress(@RequestBody routineProgress: UpdateProgress): ResponseEntity<String> {
        val response = progressService.updateRoutineProgress(routineProgress)
        return ResponseEntity.ok(response)
    }
}