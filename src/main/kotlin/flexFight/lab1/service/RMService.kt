package flexFight.lab1.service

import flexFight.lab1.entity.RM
import flexFight.lab1.entity.SetRM
import flexFight.lab1.repository.RMRepository
import org.springframework.stereotype.Service

@Service
class RMService(private val rmRepository: RMRepository) {

    fun setRM(rm: SetRM) {
        val rmEntity = rmRepository.findByUserIdAndExerciseId(rm.userId,rm.exerciseId)
        if (rmEntity != null) {
            rmEntity.reps = rm.reps
            rmRepository.save(rmEntity)
        } else {
            val newRM = RM(
                userId = rm.userId,
                exerciseId = rm.exerciseId,
                reps = rm.reps,
                date = rm.date
            )
            rmRepository.saveAndFlush(newRM)
        }
    }
}