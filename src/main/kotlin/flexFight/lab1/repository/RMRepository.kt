package flexFight.lab1.repository

import flexFight.lab1.entity.RM
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RMRepository: JpaRepository<RM,String> {
    fun findByUserIdAndExerciseId(userId: String, exerciseId: String): RM?
}