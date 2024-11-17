package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.pigletsinc.syncplay.user.repository.UserProfileRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userProfileRepository: UserProfileRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val googleOauthRepository: GoogleOauthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(userDto: UserRegistrationDto) {
        val googleUser = googleOauthRepository.findByEmailIgnoreCase(userDto.email)

        val userProfile =
            if (googleUser != null) {
                googleUser.userProfile
            } else {
                val newUserProfile = UserProfile(name = userDto.name)
                userProfileRepository.save(newUserProfile)
            }

        val userCredentials =
            UserCredentials(
                email = userDto.email,
                password = passwordEncoder.encode(userDto.password),
                userProfile = userProfile,
            )
        userCredentialsRepository.save(userCredentials)
    }
}
