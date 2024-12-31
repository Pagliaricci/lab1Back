package flexFight.lab1.controller

import flexFight.lab1.entity.CreateRoutine
import flexFight.lab1.entity.Routine
import flexFight.lab1.entity.RoutineExercise
import flexFight.lab1.service.RoutineService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routines")
class RoutineController(
    private val routineService: RoutineService
) {

@PostMapping("/create")
fun createRoutine(@RequestBody createRoutine: CreateRoutine): ResponseEntity<String> {
    routineService.createRoutine(createRoutine)
    return ResponseEntity.ok("Routine created successfully")
}

    @GetMapping("/exercises")
    fun getExercises(@RequestBody routineId : String): ResponseEntity<List<RoutineExercise>> {
        val routines = routineService.getRoutineExercises(routineId)
        return ResponseEntity.ok(routines)
    }

    @GetMapping("/get")
    fun getRoutines(@RequestParam userID: String): ResponseEntity<List<Routine>> {
        println("Getting routines for user $userID")
        val routines = routineService.getRoutines(userID)
        println("Routines: $routines")
        return ResponseEntity.ok(routines)
    }

    @GetMapping("/routineExercises")
    fun getRoutineExercises(@RequestParam routineId: String): ResponseEntity<List<RoutineExercise>> {
        val routineExercises = routineService.getRoutineExercises(routineId)
        return ResponseEntity.ok(routineExercises)
    }
}

