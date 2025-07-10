package flexFight.lab1.repository

import flexFight.lab1.entity.ExerciseProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ExerciseProgressRepository: JpaRepository<ExerciseProgress, String> {
    fun findByUserIdAndRoutineIdAndRoutineExerciseIdOrderByDateDesc(userId: String, routineId: String, routineExerciseId: String): ExerciseProgress?
    fun findByUserIdAndRoutineId(userId: String, routineId: String): List<ExerciseProgress>

    @Modifying
    @Transactional
    @Query("DELETE FROM ExerciseProgress ep WHERE ep.userId = :userId AND ep.routineId = :routineId")
    fun deleteByUserIdAndRoutineId(userId: String, routineId: String)
}