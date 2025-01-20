package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.ExerciseRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import flexFight.lab1.repository.RoutineRepository
import flexFight.lab1.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val routineExerciseRepository: RoutineExerciseRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val progressService: ProgressService
) {

    @Transactional
    fun createRoutine(createRoutine: CreateRoutine): Routine {
        // Create the Routine entity from the CreateRoutine DTO
        val routine = Routine(createRoutine, exerciseRepository)

        // Save the Routine first
        routineRepository.save(routine)

        createRoutine.exercises.forEach { dto ->
            val exercise = exerciseRepository.findById(dto.exerciseId)
                .orElseThrow { IllegalArgumentException("Exercise with ID ${dto.exerciseId} not found") }

            val routineExercise = RoutineExercise(
                exercise = exercise,
                routine = routine,
                sets = dto.sets.toString(),
                reps = dto.reps.toString(),
                day = dto.day
            )

            routineExerciseRepository.save(routineExercise)
        }

        return routine
    }

    fun getRoutineExercises(routineId: String): List<RoutineExercise> {
        return routineExerciseRepository.findByRoutineId(routineId)
    }

fun getRoutines(userID: String): Array<Routine> {
    val subscriptions = subscriptionRepository.findByUserId(userID)
    val subscribedRoutines = subscriptions.map { subscription ->
        val routine = routineRepository.findById(subscription.routineId).get()
        if (subscription.isActive) {
            routine.isActive = true
        }
        routine
    }
    val selfRoutines = routineRepository.findByCreatorOrderByCreatedAtDesc(userID)

    // Merge both lists and convert to an array
    return (subscribedRoutines + selfRoutines).toTypedArray()
}
    fun activateRoutine(routineId: String, userId: String) {
        val subscription = subscriptionRepository.findByUserIdAndRoutineId(userId, routineId)
        if (subscription != null){
            subscription.isActive = true
            subscriptionRepository.saveAndFlush(subscription)
            return 
        }
        val routine = routineRepository.findById(routineId)
            .orElseThrow { IllegalArgumentException("Routine with ID $routineId not found") }
        routine.isActive = true
        routineRepository.saveAndFlush(routine)
    }
    fun deactivateUserRoutines(userID: String) {
        val routines = routineRepository.findByCreatorOrderByCreatedAtDesc(userID)
        routines.forEach { routine ->
            routine.isActive = false
            deleteRoutineProgress(routine.id, userID)
            routineRepository.saveAndFlush(routine)
        }
        val subscriptions = subscriptionRepository.findByUserId(userID)
        subscriptions.forEach { subscription -> 
            val sub = subscriptionRepository.findByUserIdAndRoutineId(subscription.userId, subscription.routineId)
                ?: throw IllegalArgumentException("Routine with ID ${subscription.routineId} and creator ${subscription.userId} not found")
            sub.isActive = false
            sub.progress = null
            deleteRoutineProgress(sub.routineId, sub.userId)
            subscriptionRepository.saveAndFlush(sub)
        }
    }

    fun deleteRoutineProgress(routineId: String, userId: String) {
        progressService.deleteRoutineProgress(userId, routineId)
        progressService.deleteExerciseProgress(userId, routineId)
    }

    fun getActiveRoutine(userId: String): FullRoutine? {
        val routine = routineRepository.findByCreatorAndIsActive(userId, true)
        if (routine!= null){
            val routineExercises = routineExerciseRepository.findByRoutineId(routine.id)
            val fullRoutineExercises = routineExercises.map { routineExercise ->
                FullRoutineExercise(
                    name = routineExercise.exercise.name,
                    description = routineExercise.exercise.description,
                    category = routineExercise.exercise.category,
                    routine = routineExercise.routine,
                    id = routineExercise.id,
                    sets = routineExercise.sets,
                    reps = routineExercise.reps,
                    day = routineExercise.day
                )
            }
            val fullRoutine = FullRoutine(
                id = routine.id,
                name = routine.name,
                duration = routine.duration,
                intensity = routine.intensity,
                price = routine.price,
                creator = routine.creator,
                isActive = routine.isActive,
                createdAt = routine.createdAt,
                exercises = fullRoutineExercises,
                rating = routine.rating
            )
            return fullRoutine
        }
        val subscriptions = subscriptionRepository.findByUserId(userId)
        return getActiveSubscription(subscriptions)
    }

    private fun getActiveSubscription(subs: List<Subscription>): FullRoutine? {
        if (subs.isEmpty()) {
            return null
        }
        val subscription = subs.first()
        if (subscription.isActive){
            val routine = routineRepository.findById(subscription.routineId)
                .orElseThrow { IllegalArgumentException("Routine with ID ${subscription.routineId} not found") }
            val routineExercises = routineExerciseRepository.findByRoutineId(routine.id)
            val fullRoutineExercises = routineExercises.map { routineExercise ->
                FullRoutineExercise(
                    name = routineExercise.exercise.name,
                    description = routineExercise.exercise.description,
                    category = routineExercise.exercise.category,
                    routine = routineExercise.routine,
                    id = routineExercise.id,
                    sets = routineExercise.sets,
                    reps = routineExercise.reps,
                    day = routineExercise.day
                )
            }
            val fullRoutine = FullRoutine(
                id = routine.id,
                name = routine.name,
                duration = routine.duration,
                intensity = routine.intensity,
                price = routine.price,
                creator = routine.creator,
                isActive = routine.isActive,
                createdAt = routine.createdAt,
                exercises = fullRoutineExercises,
                rating = routine.rating
            )
            return fullRoutine
        }
        return getActiveSubscription(subs.drop(1))
    }


    @Transactional
    fun deleteRoutineById(id: String) {
        routineExerciseRepository.deleteAllByRoutineId(id)
        routineRepository.deleteById(id)
    }
    fun deactivateRoutine(deactivateRoutine: DeactivateRoutine) {
        val subscription = subscriptionRepository.findByUserIdAndRoutineId(deactivateRoutine.userId, deactivateRoutine.routineId)
        if (subscription != null){
            subscription.isActive = false
            subscription.progress = null
            deleteRoutineProgress(deactivateRoutine.userId, deactivateRoutine.routineId)
            subscriptionRepository.saveAndFlush(subscription)
            return
        }
        deleteRoutineProgress(deactivateRoutine.userId, deactivateRoutine.routineId)
        val routine = routineRepository.findById(deactivateRoutine.routineId)
            .orElseThrow { IllegalArgumentException("Routine with ID ${deactivateRoutine.routineId} not found") }
        routine.isActive = false
        routineRepository.saveAndFlush(routine)
    }

    fun getAll(): List<Routine> {
        return routineRepository.findAll()
    }

    fun getRoutine(id: String): Routine {
        return routineRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Routine with ID $id not found") }
    }

    @Transactional
   fun rateRoutine(rateRoutine: RateRoutine) {
        val routine = routineRepository.findById(rateRoutine.routineId)
            .orElseThrow { IllegalArgumentException("Routine with ID ${rateRoutine.routineId} not found") }
       if (routine.rating == 0.0){
              routine.rating = rateRoutine.rating
         } else {
              routine.rating = (routine.rating + rateRoutine.rating) / 2
         }
       val subscription = subscriptionRepository.findByUserIdAndRoutineId(rateRoutine.userId, rateRoutine.routineId)
         if (subscription != null){
             subscriptionRepository.deleteByUserIdAndRoutineId(rateRoutine.userId, rateRoutine.routineId)
             deleteRoutineProgress(subscription.userId, subscription.routineId)
             return
            }
       deleteRoutineProgress(rateRoutine.routineId, rateRoutine.userId)
       routine.isActive = false
       routineRepository.saveAndFlush(routine)
   }

    fun getExerciseName(id:String):String{
        return routineExerciseRepository.findById(id).get().exercise.name
    }

}
