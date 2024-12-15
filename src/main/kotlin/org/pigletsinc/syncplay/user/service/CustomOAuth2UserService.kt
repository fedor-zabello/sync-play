package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.entity.GoogleOauth
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.pigletsinc.syncplay.user.repository.UserProfileRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
    private val userProfileRepository: UserProfileRepository,
    private val googleOauthRepository: GoogleOauthRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private val delegate = DefaultOAuth2UserService()

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = delegate.loadUser(userRequest)

        val attributes = oAuth2User.attributes
        val email = attributes["email"] as String
        val oauthId = attributes["sub"] as String

        var googleUser = googleOauthRepository.findByEmailIgnoreCase(email)

        if (googleUser == null) {
            val existingCredentials = userCredentialsRepository.findByEmailIgnoreCase(email)
            if (existingCredentials != null) {
                googleUser =
                    GoogleOauth(
                        oauthId = oauthId,
                        userProfile = existingCredentials.userProfile,
                        email = email,
                    )
                googleOauthRepository.save(googleUser)
            } else {
                val userProfile = UserProfile(name = attributes["name"] as String)
                userProfileRepository.save(userProfile)

                googleUser =
                    GoogleOauth(
                        oauthId = oauthId,
                        email = email,
                        userProfile = userProfile,
                    )
                googleOauthRepository.save(googleUser)
            }
        }
        return oAuth2User
    }
}
