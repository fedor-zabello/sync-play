import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletResponse
import org.pigletsinc.syncplay.user.entity.UserRegistrationDto

@Controller
class RegistrationController(
    private val userService: UserService
) {
    @GetMapping("/auth/registration")
    fun showRegistrationForm() = "registration"


    @PostMapping("/auth/registration")
    fun registrationUser(@RequestBody userDto: UserRegistrationDto, response: HttpServletResponse) {
        userService.registrationUser(userDto)
        response.sendRedirect("/login")  // Redirect to login page
    }
}
