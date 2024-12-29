package flexFight.lab1.repository

import flexFight.lab1.entity.RoutineExercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineExerciseRepository: JpaRepository<RoutineExercise,String> {
    fun findByRoutineId(routineId: String): List<RoutineExercise>
}