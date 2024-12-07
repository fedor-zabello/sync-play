package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.UserDto
import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/registration")
    fun registerUser(
        @RequestBody userDto: UserRegistrationDto,
    ): ResponseEntity<Map<String, String>> {
        userService.registerUser(userDto)
        val response = mapOf("message" to "Registration successful")
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun getLoggedInUserDto(principal: Principal): UserDto {
        return userService.getUserDto(principal)
    }
}
