package org.pigletsinc.syncplay.channel.repository

import org.assertj.core.api.Assertions.assertThat
import org.pigletsinc.syncplay.channel.entity.Channel
import org.pigletsinc.syncplay.channel.entity.UserChannelMembership
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test

@DataJpaTest(excludeAutoConfiguration = [FlywayAutoConfiguration::class])
class ChannelRepositoryTest
    @Autowired
    constructor(
        val entityManager: TestEntityManager,
        val channelRepository: ChannelRepository,
        val userChannelMembership: UserChannelMembershipRepository,
    ) {
        @Test
        fun `Channels are persisted correctly`() {
            val prawnsClubChannel = Channel(name = "prawns club")
            entityManager.persist(prawnsClubChannel)
            entityManager.flush()
            val foundChannels = channelRepository.findAll()
            assertThat(foundChannels).contains(prawnsClubChannel)
        }

    @Test
        fun `Channel memberships are persisted correctly`() {
            val prawnsClubChannel = Channel(name = "prawns club")
            val prawnsClubMembership = UserChannelMembership(userProfileId = 1, channel = prawnsClubChannel)
            entityManager.persist(prawnsClubChannel)
            entityManager.persist(prawnsClubMembership)
            entityManager.flush()
            val foundMemberships = userChannelMembership.findAll()
            assertThat(foundMemberships).contains(prawnsClubMembership)
        }
    }
