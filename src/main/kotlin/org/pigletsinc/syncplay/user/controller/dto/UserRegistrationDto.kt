package org.pigletsinc.syncplay.user.controller.dto

data class UserRegistrationDto(
    val name: String,
    val email: String,
    val password: String,
)