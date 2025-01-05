package flexFight.lab1.service

import flexFight.lab1.entity.RM
import flexFight.lab1.entity.RMObjective
import flexFight.lab1.entity.SetRM
import flexFight.lab1.repository.RMRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RMService(private val rmRepository: RMRepository) {

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

}