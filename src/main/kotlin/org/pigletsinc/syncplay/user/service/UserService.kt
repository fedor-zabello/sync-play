package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.pigletsinc.syncplay.user.repository.UserProfileRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userProfileRepository: UserProfileRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(userDto: UserRegistrationDto) {
        // todo
        val userProfile =
            UserProfile(
                name = userDto.name,
            )
        val savedUserProfile = userProfileRepository.save(userProfile)

        val userCredentials =
            UserCredentials(
                email = userDto.email,
                password = passwordEncoder.encode(userDto.password),
                userProfile = savedUserProfile,
            )

        userCredentialsRepository.save(userCredentials)
    }
}
