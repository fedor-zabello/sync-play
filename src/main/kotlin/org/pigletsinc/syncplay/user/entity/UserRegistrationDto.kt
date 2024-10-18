package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

data class UserRegistrationDto(
    val name: String,
    val email: String,
    val password: String
)