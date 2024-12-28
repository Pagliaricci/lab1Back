package flexFight.lab1.repository

import flexFight.lab1.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, String>{
    fun findByNameContainingIgnoreCase(search: String): List<Exercise>
    fun findByCategory(category: String): List<Exercise> // New method

}
