package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.HistoryExerciseRepository
import flexFight.lab1.repository.RoutineExerciseRepository
import flexFight.lab1.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService(
    private val userService: UserService,
    private val routineService: RoutineService,
    private val progressService: ProgressService,
    private val subscriptionRepository: SubscriptionRepository,
    private val historyRepository: HistoryExerciseRepository,
    private val routineExerciseRepository: RoutineExerciseRepository,

    ) {

    fun getAllCourses(): List<FullRoutine> {
        val routines = routineService.getAll().filter { isCourse(it.creator) }
        val courses = routines.map {
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
        return courses
    }

    private fun isCourse(creator: String): Boolean {
        val a =  userService.getUser(creator).role == "Trainer"
        return a
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
        // Create subscriber
        val subscription = Subscription(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            routineId = routine.id,
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
        val subscription = subscriptionRepository.findByUserIdAndRoutineId(userId, routineId)
            ?: throw IllegalArgumentException("No subscription found for user $userId in routine $routineId")
        if (subscription.progress == null || subscription.realizedExercises.isEmpty()) {
            println("llegue aca y no deberia")
            return SubscriberWithProgress(
                userId = userId,
                username = userService.getUser(userId).username,
                routineId = routineId,
                progress = subscription.progress,
                exercisesProgress = emptyList()
            )
        }
        println("llegue aca y  deberia")
        val exerciseProgress: List<ExerciseProgressWithDetails> = subscription.realizedExercises.map { exerciseHistory ->
            val exercise = routineExerciseRepository.findById(exerciseHistory.exerciseId).get()
            val history = historyRepository.findByUserIdAndExerciseIdAndRoutineId(userId, exerciseHistory.exerciseId, routineId)
                ?: throw IllegalArgumentException("No history found for user $userId in exercise ${exerciseHistory.exerciseId} in routine $routineId")
            ExerciseProgressWithDetails(
                exerciseName = exercise.exercise.name,
                sets = history.sets,
                reps = history.reps,
                weight = history.weight,
                date = history.date,
                )
        }
        return SubscriberWithProgress(
            userId = userId,
            username = userService.getUser(userId).username,
            routineId = routineId,
            progress = subscription.progress,
            exercisesProgress = exerciseProgress
        )
    }

    fun isSubscribed(userId: String, routineId: String): Boolean {
        return subscriptionRepository.findByUserIdAndRoutineId(userId, routineId) != null
    }


}
