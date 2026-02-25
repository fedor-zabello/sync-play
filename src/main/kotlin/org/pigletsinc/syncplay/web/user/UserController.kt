package org.pigletsinc.syncplay.web.user

import jakarta.validation.Valid
import org.pigletsinc.syncplay.application.user.dto.UserDto
import org.pigletsinc.syncplay.application.user.dto.UserRegistrationDto
import org.pigletsinc.syncplay.application.user.service.UserApplicationService
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
    private val userApplicationService: UserApplicationService,
) {
    @PostMapping("/registration")
    fun registerUser(
        @Valid @RequestBody userDto: UserRegistrationDto,
    ): ResponseEntity<Map<String, String>> {
        userApplicationService.registerUser(userDto)
        val response = mapOf("message" to "Registration successful")
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun getLoggedInUserDto(principal: Principal): UserDto = userApplicationService.getUserDto(principal)
}
