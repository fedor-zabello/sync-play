package org.pigletsinc.syncplay.user.repository

import org.assertj.core.api.Assertions.assertThat
import org.pigletsinc.syncplay.user.entity.Channel
import org.pigletsinc.syncplay.user.entity.GoogleOauth
import org.pigletsinc.syncplay.user.entity.UserChannelMembership
import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.pigletsinc.syncplay.user.entity.UserProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test

@DataJpaTest(excludeAutoConfiguration = [FlywayAutoConfiguration::class])
class UserRepositoryTest
    @Autowired
    constructor(
        val entityManager: TestEntityManager,
        val userCredentialsRepository: UserCredentialsRepository,
        val userProfileRepository: UserProfileRepository,
        val googleOauthRepository: GoogleOauthRepository,
        val userChannelMembershipRepository: UserChannelMembershipRepository,
    ) {
        @Test
        fun `UserCredentials are persisted correctly`() {
            val mrPigletProfile = UserProfile(name = "mr. Piglet")
            val mrPigletCreds = UserCredentials(email = "pig@let.com", password = "pass", userProfile = mrPigletProfile)
            entityManager.persist(mrPigletProfile)
            entityManager.persist(mrPigletCreds)
            entityManager.flush()
            val foundCredentials = userCredentialsRepository.findAll()
            assertThat(foundCredentials).contains(mrPigletCreds)
        }

        @Test
        fun `GoogleOauth is persisted correctly`() {
            val mrPigletProfile = UserProfile(name = "mr. Piglet")
            val mrPigletGoogleOauth =
                GoogleOauth(oauthId = "oauth-id", email = "pig@let.com", userProfile = mrPigletProfile)
            entityManager.persist(mrPigletProfile)
            entityManager.persist(mrPigletGoogleOauth)
            entityManager.flush()
            val foundGoogleOauth = googleOauthRepository.findAll()
            assertThat(foundGoogleOauth).contains(mrPigletGoogleOauth)
        }

        @Test
        fun `UserProfile is persisted correctly`() {
            val mrPigletProfile = UserProfile(name = "mr. Piglet")
            entityManager.persist(mrPigletProfile)
            entityManager.flush()
            val foundProfile = userProfileRepository.findAll()
            assertThat(foundProfile).contains(mrPigletProfile)
        }

        @Test
        fun `Channel memberships are persisted correctly`() {
            val mrPigletProfile = UserProfile(name = "mr. Piglet")
            val prawnsClubChannel = Channel(name = "prawns-club")
            val prawnsClubMembership = UserChannelMembership(channel = prawnsClubChannel, userProfile = mrPigletProfile)
            entityManager.persist(mrPigletProfile)
            entityManager.persist(prawnsClubChannel)
            entityManager.persist(prawnsClubMembership)
            entityManager.flush()
            val foundMemberships = userChannelMembershipRepository.findAll()
            assertThat(foundMemberships).contains(prawnsClubMembership)
        }
    }
