package flexFight.lab1.repository

import flexFight.lab1.entity.Subscription
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, String> {
    fun findByUserIdAndRoutineId(userId: String, routineId: String): Subscription?
    fun findByRoutineId(routineId: String): List<Subscription>
    fun findByUserId(userId: String): List<Subscription>
    @Modifying
    @Transactional
    fun deleteByUserIdAndRoutineId(userId: String, routineId: String)
    @Modifying
    @Transactional
    fun deleteByRoutineId(routineId: String)
    fun findAllByRoutineId(trainerId: String): List<Subscription>
    fun findAllByUserId(userId: String): List<Subscription>

}
