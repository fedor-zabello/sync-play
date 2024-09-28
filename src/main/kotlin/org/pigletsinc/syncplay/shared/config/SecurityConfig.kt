package org.pigletsinc.syncplay.shared.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class BasicConfiguration {
    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): InMemoryUserDetailsManager {
        val user: UserDetails = User.withUsername("micropiglet")
            .password(passwordEncoder.encode("microcarrot"))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .anyRequest().authenticated()
            }
            // Enable form login with username/password
            .formLogin { form ->
                form
                    .loginPage("/login")  // Custom login page for username/password
                    .defaultSuccessUrl("/watch", true)  // Redirect to home after form login
                    .permitAll()  // Allow everyone to see the login page
            }
            // Enable Google OAuth2 login
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login")  // Same custom login page
                    .defaultSuccessUrl("/watch", true)  // Redirect to home after Google login
                    .permitAll()
            }
            // Enable logout support
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        return encoder
    }
}