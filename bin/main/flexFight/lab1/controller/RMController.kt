package flexFight.lab1.controller

import flexFight.lab1.entity.*
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

        if (userId.isNullOrEmpty() || weight.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weightHistory = rmService.setWeight(userId, weight.toDouble())
            ResponseEntity.ok("Weight set successfully")
        } catch (e: Exception) {
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
        val isHigherObj = request["isHigher"]

        if (userId.isNullOrEmpty() || objective.isNullOrEmpty() || isHigherObj.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }
        return try {
            rmService.setWeightObjective(userId, objective.toDouble(), !isHigherObj.toBoolean())
            ResponseEntity.ok("Weight set successfully")
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-current-weight")
    fun getCurrentWeight(@RequestBody request: Map<String, String>): ResponseEntity<Double?> {
        val userId = request["userId"]
        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weight = rmService.getCurrentWeight(userId)
            ResponseEntity.ok(weight)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/get-weight-objective")
    fun getWeightObjective(@RequestBody request: Map<String, String>): ResponseEntity<Objective> {
        val userId = request["userId"]
        if (userId.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        return try {
            val weight = rmService.getWeightObjective(userId)
            ResponseEntity.ok(weight)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/set-days-trained-objective")
    fun setDaysTrainedObjective(@RequestBody request: Map<String, String>): ResponseEntity<String> {
        val userId = request["userId"]
        val objective = request["objective"]

        if (userId.isNullOrEmpty() || objective.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build()
        }
        return try {
            rmService.setDaysTrainedObjective(userId, objective.toInt())
            ResponseEntity.ok("Days trained objective set successfully")
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

   @PostMapping("/get-days-trained-objective")
   fun getDaysTrainedObjective(@RequestBody request: Map<String, String>): ResponseEntity<Int> {
       val userId = request["userId"]
       if (userId.isNullOrEmpty()) {
           return ResponseEntity.badRequest().build()
       }
       return try {
           val objective = rmService.getDaysTrainedObjective(userId)
           ResponseEntity.ok(objective!!.objective)
       } catch (e: Exception) {
        ResponseEntity.badRequest().build()
       }
   }

    @PostMapping("/set-objective-record")
    fun setObjectiveRecord(@RequestBody request: Map<String, Any>): ResponseEntity<String> {
        val userId = request["userId"] as? String
        val date = request["date"] as? String
        val objectiveName = request["objectiveName"] as? String
        val objectiveValue = request["objectiveValue"] as? String
        val currentValue = request["currentValue"] as? String
        println(userId)
        println(date)
        println(objectiveName)
        println(objectiveValue)
        println(currentValue)
        if (userId.isNullOrEmpty() || date.isNullOrEmpty() || objectiveName.isNullOrEmpty() || objectiveValue == null || currentValue == null) {
            return ResponseEntity.badRequest().body("Invalid request parameters")
        }

        return try {
            rmService.setObjectiveRecord(userId, date, objectiveName, objectiveValue.toDouble(), currentValue.toDouble())
            ResponseEntity.ok("Objective record set successfully")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Failed to set objective record")
        }
    }

    @GetMapping("/getAll-objective-record")
    fun getAllObjectiveRecord(@RequestParam userId: String): ResponseEntity<List<ObjRecord>> {
        return try {
            val objectiveRecord = rmService.getAllUserObjectiveRecords(userId)
            ResponseEntity.ok(objectiveRecord)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}