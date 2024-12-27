package flexFight.lab1.controller

import flexFight.lab1.entity.CreateRoutine
import flexFight.lab1.entity.Routine
import flexFight.lab1.service.RoutineService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routines")
class RoutineController(
    private val routineService: RoutineService
) {

    @PostMapping
    fun createRoutine(@RequestBody createRoutine: CreateRoutine): ResponseEntity<Routine> {
        val routine = routineService.createRoutine(createRoutine)
        return ResponseEntity.ok(routine)
    }
}
