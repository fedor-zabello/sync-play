package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.entity.GoogleOauth
import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.pigletsinc.syncplay.user.repository.UserProfileRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userProfileRepository: UserProfileRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val googleOAuthRepository: GoogleOauthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    // Method for registration with email and password
    fun registrationUser(userDto: UserRegistrationDto) {
        // Create and save UserProfile
        val userProfile =
            UserProfile(
                name = userDto.email,
            )
        val savedUserProfile = userProfileRepository.save(userProfile)

        // Create UserCredentials with hashed password
        val userCredentials =
            UserCredentials(
                email = userDto.email,
                password = passwordEncoder.encode(userDto.password), // Hashing the password
                userProfile = savedUserProfile,
            )

        // Save UserCredentials
        userCredentialsRepository.save(userCredentials)
    }

    // Method for registration with Google OAuth
    fun registrationGoogleUser(
        oauthId: String,
        name: String,
    ) {
        // Create and save UserProfile
        val userProfile =
            UserProfile(
                name = name,
            )
        val savedUserProfile = userProfileRepository.save(userProfile)

        // Create GoogleOAuth record
        val googleOauth =
            GoogleOauth(
                oauthId = oauthId,
                userProfile = savedUserProfile,
            )

        // Save GoogleOAuth
        googleOAuthRepository.save(googleOauth)
    }
}
