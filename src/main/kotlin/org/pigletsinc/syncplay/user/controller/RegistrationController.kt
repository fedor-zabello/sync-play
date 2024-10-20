package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api/v1/users")
class RegistrationController(
    private val userService: UserService,
) {
    @PostMapping("/registration")
    @ResponseBody
    fun registrationUser(
        @RequestBody userDto: UserRegistrationDto,
    ): ResponseEntity<String> {
        userService.registrationUser(userDto)
        return ResponseEntity.ok("Registration successful")
    }
}
