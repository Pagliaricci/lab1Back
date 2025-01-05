package flexFight.lab1.controller

import flexFight.lab1.entity.RM
import flexFight.lab1.entity.SetRM
import flexFight.lab1.service.RMService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rm")
class RMController(private val rmService: RMService){

    @PostMapping("/set-rm")
    fun setRM(@RequestBody rm: SetRM): ResponseEntity<Double> {
        return try {
            val oneRepMax = rmService.setRM(rm)
            ResponseEntity.ok(oneRepMax)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-rm")
    fun getRM(@RequestBody request: Map<String, String>): ResponseEntity<RM?> {
        val exerciseId = request["exerciseId"]
        val userId = request["userId"]
        println("Received getRM request with: exerciseId=$exerciseId, userId=$userId")

        if (exerciseId.isNullOrEmpty() || userId.isNullOrEmpty()) {
            println("Bad request: Missing parameters.")
            return ResponseEntity.badRequest().build()
        }

        return try {
            val rm = rmService.getRM(userId, exerciseId)
            if (rm != null) {
                println("Found RM: $rm")
                ResponseEntity.ok(rm)
            } else {
                println("No RM found for exerciseId=$exerciseId and userId=$userId")
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            println("Error processing request: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

}