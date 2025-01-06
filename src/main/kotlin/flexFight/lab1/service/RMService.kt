package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.DaysTrainedObjectiveRepository
import flexFight.lab1.repository.RMRepository
import flexFight.lab1.repository.UserRepository
import flexFight.lab1.repository.weightHistoryRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RMService(private val rmRepository: RMRepository, private val weightHistoryRepository: weightHistoryRepository, private val userRepository: UserRepository, private val daysTrainedObjectiveRepository: DaysTrainedObjectiveRepository) {

    fun setRM(rm: SetRM): Double {
        if (rm.reps <= 0) throw IllegalArgumentException("Reps must be greater than 0")

        val oneRepMax = rm.weight * (1 + rm.reps / 30.0)

        saveOneRepMax(rm.userId, rm.exerciseId, oneRepMax, rm.date)

        return oneRepMax
    }

    private fun saveOneRepMax(userId: String, exerciseId: String, oneRepMax: Double, date: Date) {
        val currentRM = rmRepository.findFirstByUserIdAndExerciseIdOrderByDateDesc(userId, exerciseId)
        if (currentRM != null && currentRM.rm >= oneRepMax) return
        if (currentRM != null && currentRM.rm == 0.0) updateRM(currentRM, oneRepMax, date)
        else if (currentRM != null) {
            createRM(userId, exerciseId, oneRepMax, date, currentRM.objective)
        }
        else createRM(userId, exerciseId, oneRepMax, date, 0.0)
    }

    private fun createRM(userId: String, exerciseId: String, oneRepMax: Double, date: Date, objective: Double) {
        val rm = RM(userId = userId, exerciseId = exerciseId, rm = oneRepMax, date = date, objective = objective)
        rmRepository.save(rm)
    }
    fun updateRM(rm: RM, oneRepMax: Double, date: Date) {
        rm.rm = oneRepMax
        rm.date = date
        rmRepository.save(rm)
    }
    fun getRM(userId: String, exerciseId: String): RM? {
        return rmRepository.findFirstByUserIdAndExerciseIdOrderByDateDesc(userId, exerciseId)
    }

    fun getRMHistory(userId: String, exerciseId: String): List<RM> {
        return rmRepository.findAllByUserIdAndExerciseIdOrderByDateAsc(userId, exerciseId)
    }
    fun setRMObjective(rmObjective: RMObjective): RM {
        val rm = rmRepository.findFirstByUserIdAndExerciseIdOrderByDateDesc(rmObjective.userId, rmObjective.exerciseId)
        if (rm != null) {
            rm.objective = rmObjective.objective
            rmRepository.save(rm)
            return rm
        } else {
            val newRM = RM(userId = rmObjective.userId, exerciseId = rmObjective.exerciseId, objective = rmObjective.objective)
            rmRepository.save(newRM)
            return newRM
        }
    }

    fun setWeight(userId: String, weight: Double) {
        val weightHistory = weightHistoryRepository.findFirstByUserIdOrderByDateDesc(userId)
        if (weightHistory != null) {
            val newWeightHistory = WeightHistory(userId = userId, weight = weight, objective = weightHistory.objective, isHigherObj = weightHistory.isHigherObj)
            weightHistoryRepository.save(newWeightHistory)
        } else {
            val newWeightHistory = WeightHistory(userId = userId, weight = weight)
            weightHistoryRepository.save(newWeightHistory)
        }
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        user.weight = weight.toString()
        userRepository.save(user)
    }


    fun getWeightHistory(userId: String): List<WeightHistory> {
        return weightHistoryRepository.findAllByUserIdOrderByDateAsc(userId)
    }

    fun setWeightObjective(userId: String, objective: Double, isHigherObj: Boolean) {
        val weightHistory = weightHistoryRepository.findFirstByUserIdOrderByDateDesc(userId)
        if (weightHistory != null) {
            weightHistory.objective = objective
            weightHistory.isHigherObj = isHigherObj
            weightHistoryRepository.save(weightHistory)
        } else {
            val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
            val newWeightHistory = WeightHistory(userId = userId, weight = user.weight.toDouble(), objective = objective, isHigherObj = isHigherObj)
            weightHistoryRepository.save(newWeightHistory)
        }
    }

    fun getCurrentWeight(userId: String): Double {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        return user.weight.toDouble()
    }

    fun getWeightObjective(userId: String): Objective {
        val weightHistory = weightHistoryRepository.findFirstByUserIdOrderByDateDesc(userId)
        return Objective(weightHistory?.objective ?: 0.0, weightHistory?.isHigherObj ?: false)
    }
fun setDaysTrainedObjective(userId: String, objective: Int) {
    val oldObjective = daysTrainedObjectiveRepository.findByUserId(userId)
    println("oldObjective: $oldObjective")
    if (oldObjective != null) {
        oldObjective.objective = objective
        daysTrainedObjectiveRepository.save(oldObjective)
    } else {
        val daysTrainedObjective = DaysTrainedObjective(userId = userId, objective = objective)
        daysTrainedObjectiveRepository.save(daysTrainedObjective)
    }
}
    fun getDaysTrainedObjective(userId: String): DaysTrainedObjective?{
        return daysTrainedObjectiveRepository.findByUserId(userId)
    }
}