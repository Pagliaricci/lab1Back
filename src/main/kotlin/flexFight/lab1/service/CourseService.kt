package flexFight.lab1.service

import flexFight.lab1.entity.*
import flexFight.lab1.repository.HistoryExerciseRepository
import flexFight.lab1.repository.HistorySubscriptionRepository
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
    private val historySubscriptionRepository: HistorySubscriptionRepository

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
                ,
                it.rating
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
        println("AAAAAAAAA")
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
        historySubscriptionRepository.saveAndFlush(HistorySubscription(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            routineId = routine.id
        ))
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
            realizedExercises = historyRepository.findByUserIdAndRoutineId(it.userId,routineId)) }
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
                },
                routine.rating
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
                },
                it.rating
            )
        }
    }

    fun getProgressForUser(userId: String, routineId: String): SubscriberWithProgress {
        val subscription = subscriptionRepository.findByUserIdAndRoutineId(userId, routineId)
            ?: throw IllegalArgumentException("No subscription found for user $userId in routine $routineId")
        if (subscription.progress == null) {
            return SubscriberWithProgress(
                userId = userId,
                username = userService.getUser(userId).username,
                routineId = routineId,
                progress = subscription.progress,
                exercisesProgress = emptyList()
            )
        }
        val exerciseProgress: List<ExerciseProgressWithDetails> = historyRepository.findByUserIdAndRoutineId(userId,routineId).map { exercise -> ExerciseProgressWithDetails(exercise.id, routineService.getExerciseName(exercise.exerciseId),exercise.sets,exercise.reps,exercise.weight,exercise.date)}
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



fun getAllMySubscribers(trainerId: String): List<User> {
    val routines = routineService.getAll().filter { it.creator == trainerId }
    val subs = routines.flatMap { subscriptionRepository.findAllByRoutineId(it.id) }
    val users = subs.map { userService.getUser(it.userId) }.toSet()
    println(users)
    return users.toList()
}

    fun getAllMyTrainers(userId: String): List<User>{
        val subs = subscriptionRepository.findAllByUserId(userId)
        val courses = subs.map { routineService.getRoutine(it.routineId) }
        val trainers = courses.map { userService.getUser(it.creator) }
        println(trainers)
        return trainers
    }

    fun getHistorySubscriptions(routineId: String): List<SubscriberHistoryWithName> {
        val subs = historySubscriptionRepository.findByRoutineId(routineId)
        return subs.map { SubscriberHistoryWithName(
            it.id,
            username = userService.getUser(it.userId).username,
            it.routineId,
            it.date.date.toString() + "/" + it.date.month.toString() + "/" + "2025"
        )
        }
    }
}
