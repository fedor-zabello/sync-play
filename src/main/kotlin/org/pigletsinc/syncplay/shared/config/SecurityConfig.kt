package org.pigletsinc.syncplay.shared.config

import org.pigletsinc.syncplay.user.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/api/v1/users/registration")
            }.authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers("/registration", "/api/v1/users/registration")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/watch", true)
                    .permitAll()
            }.oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/watch", true)
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService)
                    }
            }.logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }
        return http.build()
    }
}
