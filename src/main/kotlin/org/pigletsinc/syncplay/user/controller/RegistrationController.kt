package org.pigletsinc.syncplay.user.controller

import jakarta.servlet.http.HttpServletResponse
import org.pigletsinc.syncplay.user.entity.UserRegistrationDto
import org.pigletsinc.syncplay.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class RegistrationController(
    private val userService: UserService,
) {
    @GetMapping("/auth/registration")
    fun showRegistrationForm() = "registration"

    @PostMapping("/auth/registration")
    fun registrationUser(
        @RequestBody userDto: UserRegistrationDto,
        response: HttpServletResponse,
    ) {
        userService.registrationUser(userDto)
        response.sendRedirect("/login") // Redirect to login page
    }
}
