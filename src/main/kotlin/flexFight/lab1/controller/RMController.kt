package flexFight.lab1.controller

import flexFight.lab1.entity.RM
import flexFight.lab1.entity.RMObjective
import flexFight.lab1.entity.SetRM
import flexFight.lab1.entity.WeightHistory
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

        if (exerciseId.isNullOrEmpty() || userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val rm = rmService.getRM(userId, exerciseId)
            if (rm != null) {
                ResponseEntity.ok(rm)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            println("Error processing request: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-rm-history")
    fun getRMHistory(@RequestBody request: Map<String, String>): ResponseEntity<List<RM>> {
        val exerciseId = request["exerciseId"]
        val userId = request["userId"]

        if (exerciseId.isNullOrEmpty() || userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val rmHistory = rmService.getRMHistory(userId, exerciseId)
            if (rmHistory.isNotEmpty()) {
                ResponseEntity.ok(rmHistory)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/set-rm-objective")
    fun setRMObjective(@RequestBody rmObjective: RMObjective): ResponseEntity<RM> {
        return try {
            val rm = rmService.setRMObjective(rmObjective)
            ResponseEntity.ok(rm)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/set-weight")
    fun setWeight(@RequestBody request: Map<String, String>): ResponseEntity<String> {
        val userId = request["userId"]
        val weight = request["weight"]
        println("Received setWeight request with: userId=$userId, weight=$weight")

        if (userId.isNullOrEmpty() || weight.isNullOrEmpty()) {
            println("Bad request: Missing parameters.")
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weightHistory = rmService.setWeight(userId, weight.toDouble())
            println("Weight set successfully")
            println(weightHistory.toString())
            ResponseEntity.ok("Weight set successfully")
        } catch (e: Exception) {
            println("Error processing request: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-weight-history")
    fun getWeightHistory(@RequestBody request: Map<String, String>): ResponseEntity<List<WeightHistory>> {
        val userId = request["userId"]

        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weightHistory = rmService.getWeightHistory(userId)
            if (weightHistory.isNotEmpty()) {
                ResponseEntity.ok(weightHistory)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/set-weight-objective")
    fun setWeightObjective(@RequestBody request: Map<String, String>): ResponseEntity<String> {
        val userId = request["userId"]
        val objective = request["objective"]
        println("Received setWeight request with: userId=$userId, weight=$objective")

        if (userId.isNullOrEmpty() || objective.isNullOrEmpty()) {
            println("Bad request: Missing parameters.")
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weightHistory = rmService.setWeightObjective(userId, objective.toDouble())
            println("Weight set successfully")
            println(weightHistory.toString())
            ResponseEntity.ok("Weight set successfully")
        } catch (e: Exception) {
            println("Error processing request: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-current-weight")
    fun getCurrentWeight(@RequestBody request: Map<String, String>): ResponseEntity<Double?> {
        val userId = request["userId"]
        println("Received getCurrentWeight request with: userId=$userId")
        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weight = rmService.getCurrentWeight(userId)
            println("Current weight: $weight")
            ResponseEntity.ok(weight)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-weight-objective")
    fun getWeightObjective(@RequestBody request: Map<String, String>): ResponseEntity<Double?> {
        val userId = request["userId"]
        println("Received getWeightObjective request with: userId=$userId")
        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weight = rmService.getWeightObjective(userId)
            println("Current weight: $weight")
            ResponseEntity.ok(weight)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}