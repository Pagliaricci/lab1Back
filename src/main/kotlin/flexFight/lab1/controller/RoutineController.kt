package flexFight.lab1.controller

import flexFight.lab1.entity.*
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
        val routines = routineService.getRoutines(userID)
        return ResponseEntity.ok(routines)
    }

    @GetMapping("/routineExercises")
    fun getRoutineExercises(@RequestParam routineId: String): ResponseEntity<List<RoutineExercise>> {
        val routineExercises = routineService.getRoutineExercises(routineId)
        return ResponseEntity.ok(routineExercises)
    }

    @PostMapping("/activateRoutine")
    fun activateRoutine(@RequestBody activateRoutine: ActivateRoutine): ResponseEntity<String> {
        routineService.deactivateUserRoutines(activateRoutine.userId)
        routineService.activateRoutine(activateRoutine.routineId)
        return ResponseEntity.ok("Routine activated")
    }
    @PostMapping("/deactivate")
    fun deactivateRoutine(@RequestBody deactivateRoutine: DeactivateRoutine): ResponseEntity<String> {
        routineService.deactivateRoutine(deactivateRoutine)
        return ResponseEntity.ok("Routine deactivated successfully")
    }

    @GetMapping("/getActive")
    fun getActiveRoutine(@RequestParam userId: String): ResponseEntity<FullRoutine?> {
        val routine = routineService.getActiveRoutine(userId)
        return ResponseEntity.ok(routine)
    }

    @DeleteMapping("/{id}")
    fun deleteRoutine(@PathVariable id: String): ResponseEntity<String> {
        routineService.deleteRoutineById(id)
        return ResponseEntity.ok("Routine deleted successfully")
    }
}

