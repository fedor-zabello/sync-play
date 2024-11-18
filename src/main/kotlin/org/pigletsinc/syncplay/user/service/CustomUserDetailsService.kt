package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userCredentialsRepository: UserCredentialsRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val userCredentials: UserCredentials =
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
