package flexFight.lab1.repository

import flexFight.lab1.entity.RoutineProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository

interface ProgressRepository: JpaRepository<RoutineProgress, String> {
    fun findByUserIdAndRoutineId(userId: String, routineId: String): RoutineProgress?
}