package org.pigletsinc.syncplay.application.user.service

import org.pigletsinc.syncplay.application.user.dto.UserDto
import org.pigletsinc.syncplay.application.user.dto.UserRegistrationDto
import org.pigletsinc.syncplay.application.user.mapper.toDto
import org.pigletsinc.syncplay.domain.user.service.UserService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserApplicationService(
    private val userService: UserService,
) {
    fun registerUser(dto: UserRegistrationDto) = userService.registerUser(dto.name, dto.email, dto.password)

    fun getUserDto(principal: Principal): UserDto = userService.getUserProfileByPrincipal(principal).toDto()
}
