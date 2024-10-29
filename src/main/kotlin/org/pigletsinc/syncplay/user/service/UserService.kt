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
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userProfileRepository: UserProfileRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val googleOAuthRepository: GoogleOauthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registrationUser(userDto: UserRegistrationDto) {
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

    fun registrationGoogleUser(
        oauthId: String,
        name: String,
    ) {
        val userProfile =
            UserProfile(
                name = name,
            )
        val savedUserProfile = userProfileRepository.save(userProfile)

        val googleOauth =
            GoogleOauth(
                oauthId = oauthId,
                userProfile = savedUserProfile,
            )

        googleOAuthRepository.save(googleOauth)
    }
}
