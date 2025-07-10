package flexFight.lab1.repository

import flexFight.lab1.entity.DaysTrainedObjective
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DaysTrainedObjectiveRepository: JpaRepository<DaysTrainedObjective, String> {
    fun findByUserId(userId: String): DaysTrainedObjective?
}