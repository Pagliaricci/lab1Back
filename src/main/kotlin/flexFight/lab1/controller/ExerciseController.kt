package flexFight.lab1.controller

import flexFight.lab1.entity.Exercise
import flexFight.lab1.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exercises")
class ExerciseController(
    private val exerciseService: ExerciseService
) {

    @PostMapping
    fun createExercise(@RequestBody exercise: Exercise): ResponseEntity<Exercise> {
        val savedExercise = exerciseService.createExercise(exercise)
        return ResponseEntity.ok(savedExercise)
    }
}
