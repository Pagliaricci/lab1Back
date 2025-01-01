package flexFight.lab1.controller

import flexFight.lab1.entity.AddRecord
import flexFight.lab1.service.HistoryExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/historyExercises")
class HistoryExerciseController(
    private val historyExerciseService: HistoryExerciseService
) {

    @PostMapping("/addRecord")
    fun addRecord(@RequestBody addRecord: AddRecord): ResponseEntity<String> {
        val response = historyExerciseService.addRecord(addRecord)
        return ResponseEntity.ok(response)
    }


}