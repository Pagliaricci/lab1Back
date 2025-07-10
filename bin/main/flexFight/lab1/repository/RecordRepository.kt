package flexFight.lab1.repository

import flexFight.lab1.entity.ObjRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface RecordRepository: JpaRepository<ObjRecord, String> {
    fun findAllByUserIdOrderByDateDesc(userId: String): List<ObjRecord>
    fun findByUserIdAndObjectiveValue(userId: String, objectiveValue: Double): ObjRecord?
}