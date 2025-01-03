package flexFight.lab1.repository

import flexFight.lab1.entity.HistoryExercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoryExerciseRepository: JpaRepository<HistoryExercise, String> {
    fun findByUserIdAndExerciseIdAndRoutineId(userId: String, exerciseId: String, routineId: String): HistoryExercise?
    fun findByUserId(userId: String): List<HistoryExercise>
}