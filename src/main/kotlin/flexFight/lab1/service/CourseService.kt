package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.ExerciseProgressRepository
import flexFight.lab1.repository.HistoryExerciseRepository
import flexFight.lab1.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService(
    private val userService: UserService,
    private val routineService: RoutineService,
    private val progressService: ProgressService,
    private val subscriptionRepository: SubscriptionRepository,
    private val exerciseProgressRepository: ExerciseProgressRepository,
    private val historyRepository: HistoryExerciseRepository,
    private val historyExerciseService: HistoryExerciseService,

) {

    fun getAllCourses(): List<FullRoutine> {
        val courses = routineService.getAll().filter { isCourse(it.creator) }
        return courses.map {
            FullRoutine(
                it.id,
                it.name,
                it.duration,
                it.intensity,
                it.price,
                creator = getCreatorName(it.creator),
                it.createdAt,
                it.isActive,
                routineService.getRoutineExercises(it.id).map { re ->
                    FullRoutineExercise(
                        re.exercise.name,
                        re.exercise.description,
                        re.exercise.category,
                        re.exercise.id,
                        re.routine,
                        re.sets,
                        re.reps,
                        re.day
                    )
                }
            )
        }
    }

    private fun isCourse(creator: String): Boolean {
        return userService.getUser(creator).role == "Trainer"
    }

    private fun getCreatorName(creator: String): String {
        return userService.getUser(creator).username
    }

    fun subscribeToCourse(addSubscriber: AddSubscriber): Subscription {
        val routine = routineService.getRoutine(addSubscriber.routineId)
        val user = userService.getUser(addSubscriber.userId)
        // Check if already subscribed
        val existingSubscriber = subscriptionRepository.findByUserIdAndRoutineId(user.id, routine.id)
        if (existingSubscriber != null) {
            throw IllegalArgumentException("User is already subscribed to this course")
        }

        // Start routine progress
        progressService.startRoutine(StartRoutine(routine.id, user.id))
        val exercises = exerciseProgressRepository.findByUserIdAndRoutineId(user.id, routine.id)

        // Create subscriber
        val subscription = Subscription(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            routineId = routine.id,
            progress = progressService.getRoutineProgress(GetProgress(routine.id, user.id))!!,
            realizedExercises = exercises
        )

        return subscriptionRepository.saveAndFlush(subscription)
    }

    fun getSubscribers(routineId: String): List<SubscriberWithName> {
        val subs = subscriptionRepository.findByRoutineId(routineId)
        return subs.map { SubscriberWithName(
            it.id,
            it.userId,
            username = userService.getUser(it.userId).username,
            it.routineId,
            it.progress,
            it.realizedExercises
        ) }
    }

    fun searchTrainerRoutines(trainerId: String): List<FullRoutine> {
        val trainer = userService.getUser(trainerId)
        if (trainer.role != "Trainer") {
            throw IllegalArgumentException("User is not a trainer")
        }

        val routines = routineService.getAll().filter { it.creator == trainerId }
        return routines.map { routine ->
            FullRoutine(
                routine.id,
                routine.name,
                routine.duration,
                routine.intensity,
                routine.price,
                creator = trainer.username,
                routine.createdAt,
                routine.isActive,
                routineService.getRoutineExercises(routine.id).map { re ->
                    FullRoutineExercise(
                        re.exercise.name,
                        re.exercise.description,
                        re.exercise.category,
                        re.exercise.id,
                        re.routine,
                        re.sets,
                        re.reps,
                        re.day
                    )
                }
            )
        }
    }

    fun unsubscribeFromCourse(userId: String, routineId: String): String {
        val subscriber = subscriptionRepository.findByUserIdAndRoutineId(userId, routineId)
            ?: throw IllegalArgumentException("User is not subscribed to this course")
        subscriptionRepository.delete(subscriber)
        progressService.deleteRoutineProgress(userId, routineId)
        progressService.deleteExerciseProgress(userId, routineId)
        return "Unsubscribed from course"
    }

    fun searchCourses(search: String): List<FullRoutine> {
        val courses = getAllCourses().filter { it.name.contains(search, ignoreCase = true) }
        return courses.map {
            FullRoutine(
                it.id,
                it.name,
                it.duration,
                it.intensity,
                it.price,
                it.creator,
                it.createdAt,
                it.isActive,
                routineService.getRoutineExercises(it.id).map { re ->
                    FullRoutineExercise(
                        re.exercise.name,
                        re.exercise.description,
                        re.exercise.category,
                        re.exercise.id,
                        re.routine,
                        re.sets,
                        re.reps,
                        re.day
                    )
                }
            )
        }
    }

    fun getProgressForUser(userId: String, routineId: String): SubscriberWithProgress {
        // Buscar la suscripción del usuario en la rutina específica
        val subscription = subscriptionRepository.findByUserIdAndRoutineId(userId, routineId)
            ?: throw IllegalArgumentException("No subscription found for user $userId in routine $routineId")

        // Obtener el progreso de la rutina para ese usuario
        val progress = progressService.getRoutineProgress(GetProgress(routineId, userId))
            ?: throw IllegalArgumentException("No progress found for user $userId in routine $routineId")

        // Obtener el progreso de los ejercicios desde el HistoryExercise
        val exercisesProgress = historyRepository.findByUserIdAndRoutineId(userId, routineId).map {
            ExerciseProgressWithDetails(
                exerciseName = historyExerciseService.getExerciseName(it.exerciseId),  // Obtener el nombre del ejercicio
                sets = it.sets,
                reps = it.reps,
                weight = it.weight,
                date = it.date
            )
        }

        // Crear y devolver el DTO con toda la información
        return SubscriberWithProgress(
            userId = userId,
            username = userService.getUser(userId).username,  // Obtener el nombre de usuario
            routineId = routineId,
            progress = progress,
            exercisesProgress = exercisesProgress
        )
    }


}
