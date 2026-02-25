package org.pigletsinc.syncplay.domain.user.service

import org.pigletsinc.syncplay.domain.user.entity.UserCredentials
import org.pigletsinc.syncplay.domain.user.entity.UserProfile
import org.pigletsinc.syncplay.domain.user.repository.IGoogleOauthRepository
import org.pigletsinc.syncplay.domain.user.repository.IUserCredentialsRepository
import org.pigletsinc.syncplay.domain.user.repository.IUserProfileRepository
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
    private val userProfileRepository: IUserProfileRepository,
    private val userCredentialsRepository: IUserCredentialsRepository,
    private val googleOauthRepository: IGoogleOauthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        if (userCredentialsRepository.findByEmailIgnoreCase(email) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with email $email already exists")
        }

        val googleUser = googleOauthRepository.findByEmailIgnoreCase(email)

        val userProfile =
            if (googleUser != null) {
                googleUser.userProfile
            } else {
                userProfileRepository.save(UserProfile(name = name))
            }

        val userCredentials =
            UserCredentials(
                email = email,
                password = passwordEncoder.encode(password),
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

    fun findUserProfileByName(username: String): UserProfile? = userProfileRepository.findByName(username)

    fun saveUserProfile(userProfile: UserProfile): UserProfile = userProfileRepository.save(userProfile)
}
