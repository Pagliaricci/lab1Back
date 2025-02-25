package flexFight.lab1.repository

import flexFight.lab1.entity.HistorySubscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistorySubscriptionRepository: JpaRepository<HistorySubscription, String> {
    fun findByRoutineId(routineId: String): List<HistorySubscription>
}