package flexFight.lab1.repository

import flexFight.lab1.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, String> {
    fun findByUserIdAndRoutineId(userId: String, routineId: String): Subscription?
    fun findByRoutineId(routineId: String): List<Subscription>
    fun findByUserId(userId: String): List<Subscription>
}
