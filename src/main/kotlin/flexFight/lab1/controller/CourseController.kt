package flexFight.lab1.controller

import flexFight.lab1.entity.*
import flexFight.lab1.service.CourseService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course")
class CourseController(private val courseService: CourseService) {

    @GetMapping("/all")
    fun getAllCourses(): List<FullRoutine> {
        return courseService.getAllCourses()
    }

    @PostMapping("/subscribe")
    fun subscribeToCourse(@RequestBody addSubscriber: AddSubscriber): Subscription {
        return courseService.subscribeToCourse(addSubscriber)
    }

    @GetMapping("/{routineId}/subscribers")
    fun getSubscribers(@PathVariable routineId: String): List<SubscriberWithName> {
        val response =  courseService.getSubscribers(routineId)
        return response
    }

    @GetMapping("/trainer/{trainerId}")
    fun searchTrainerRoutines(@PathVariable trainerId: String): List<FullRoutine> {
        return courseService.searchTrainerRoutines(trainerId)
    }

    @GetMapping("/subscribers/{userId}/progress/{routineId}")
    fun getProgressForUser(@PathVariable userId: String, @PathVariable routineId: String): SubscriberWithProgress {
        return courseService.getProgressForUser(userId, routineId)
    }



    @PostMapping("/unsubscribe")
    fun unsubscribeFromCourse(@RequestBody addSubscriber: AddSubscriber): String {
        return courseService.unsubscribeFromCourse(addSubscriber.userId, addSubscriber.routineId)
    }

    @GetMapping("/search")
    fun searchCourses(@RequestParam query: String): List<FullRoutine> {
        return courseService.searchCourses(query)
    }
}
