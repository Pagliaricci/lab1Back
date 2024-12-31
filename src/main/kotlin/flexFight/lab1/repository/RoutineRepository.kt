package flexFight.lab1.repository

import flexFight.lab1.entity.Routine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineRepository : JpaRepository<Routine, String>{
    fun findByCreator(userId: String): List<Routine>
}
