package org.pigletsinc.syncplay.application.user.mapper

import org.pigletsinc.syncplay.application.user.dto.UserDto
import org.pigletsinc.syncplay.domain.user.entity.UserProfile

fun UserProfile.toDto(): UserDto = UserDto(this.name)
