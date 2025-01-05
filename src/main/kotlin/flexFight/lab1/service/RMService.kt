package flexFight.lab1.service

import flexFight.lab1.entity.RM
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
        val rm = RM(userId = userId, exerciseId = exerciseId, rm = oneRepMax, date = date)
        rmRepository.save(rm)
    }

    fun getRM(userId: String, exerciseId: String): RM? {
        return rmRepository.findFirstByUserIdAndExerciseIdOrderByDateDesc(userId, exerciseId)
    }

}