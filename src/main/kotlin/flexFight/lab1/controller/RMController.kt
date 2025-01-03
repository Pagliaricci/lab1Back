package flexFight.lab1.controller

import flexFight.lab1.entity.SetRM
import flexFight.lab1.service.ProgressService
import flexFight.lab1.service.RMService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rm")
class RMController(private val rmService: RMService){

    @PostMapping("/set-rm")
    fun setRM(@RequestBody rm: SetRM): String {
        try {
            rmService.setRM(rm)
            return "RM set successfully"
        } catch (e: Exception) {
            return "Error setting RM"
        }
    }
}