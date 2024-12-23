package flexFight.lab1.controller

import flexFight.lab1.entity.CreateWorkout
import flexFight.lab1.entity.Workout
import flexFight.lab1.service.WorkoutService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workouts")
class WorkoutController(
    private val workoutService: WorkoutService
) {

    @PostMapping
    fun createWorkout(@RequestBody createWorkout: CreateWorkout): ResponseEntity<Workout> {
        val workout = workoutService.createWorkout(createWorkout)
        return ResponseEntity.ok(workout)
    }
}
