package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.UserDto
import org.pigletsinc.syncplay.user.UserRegistrationDto
import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.pigletsinc.syncplay.user.repository.UserProfileRepository
import org.pigletsinc.syncplay.user.toDto
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

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

    fun getUserProfileByPrincipal(principal: Principal): UserProfile {
        val userProfile =
            when (principal) {
                is UsernamePasswordAuthenticationToken -> userCredentialsRepository.findByEmailIgnoreCase(principal.name)?.userProfile
                is OAuth2AuthenticationToken -> googleOauthRepository.findByOauthId(principal.name)?.userProfile
                else -> throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
            }
        return userProfile ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    fun getUserDto(principal: Principal): UserDto {
        val userProfile = getUserProfileByPrincipal(principal)
        return userProfile.toDto()
    }

    fun saveUserProfile(userProfile: UserProfile) = userProfileRepository.save(userProfile)
}
