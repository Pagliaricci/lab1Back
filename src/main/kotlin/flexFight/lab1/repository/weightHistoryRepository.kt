package flexFight.lab1.repository

import flexFight.lab1.entity.WeightHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface weightHistoryRepository: JpaRepository<WeightHistory, String> {
    fun findAllByUserIdOrderByDateAsc(userId: String): List<WeightHistory>
    fun findFirstByUserIdOrderByDateDesc(userId: String): WeightHistory?
}