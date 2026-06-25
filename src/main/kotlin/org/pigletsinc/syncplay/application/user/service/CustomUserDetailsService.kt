package org.pigletsinc.syncplay.application.user.service

import org.pigletsinc.syncplay.domain.user.repository.IUserCredentialsRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userCredentialsRepository: IUserCredentialsRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val userCredentials =
            userCredentialsRepository.findByEmailIgnoreCase(email)
                ?: throw UsernameNotFoundException("User not found with email: $email")

        return User
            .builder()
            .username(userCredentials.email)
            .password(userCredentials.password)
            .roles("USER")
            .build()
    }
}
