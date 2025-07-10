package flexFight.lab1.repository

import flexFight.lab1.entity.RoutineProgress
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository

interface ProgressRepository: JpaRepository<RoutineProgress, String> {
    fun findByUserIdAndRoutineId(userId: String, routineId: String): RoutineProgress?
    @Modifying
    @Transactional
    @Query("DELETE FROM RoutineProgress rp WHERE rp.userId = :userId AND rp.routineId = :routineId")
    fun deleteByUserIdAndRoutineId(userId: String, routineId: String)
}