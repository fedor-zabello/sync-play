package org.pigletsinc.syncplay.repository.user.impl

import org.pigletsinc.syncplay.domain.user.entity.UserProfile
import org.pigletsinc.syncplay.domain.user.repository.IUserProfileRepository
import org.pigletsinc.syncplay.repository.channel.jpainterfaces.ChannelRepositoryJpa
import org.pigletsinc.syncplay.repository.user.jpainterfaces.UserProfileRepositoryJpa
import org.pigletsinc.syncplay.repository.user.mapper.toJpa
import org.pigletsinc.syncplay.repository.user.mapper.toUserProfile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserProfileRepositoryImpl(
    private val userProfileJpa: UserProfileRepositoryJpa,
    private val channelRepositoryJpa: ChannelRepositoryJpa,
) : IUserProfileRepository {
    override fun save(userProfile: UserProfile): UserProfile {
        val jpa = userProfile.toJpa()
        jpa.channels = userProfile.channels
            .mapNotNull { it.id?.let { id -> channelRepositoryJpa.getReferenceById(id) } }
            .toMutableSet()
        return userProfileJpa.save(jpa).toUserProfile()
    }

    @Transactional(readOnly = true)
    override fun findByName(username: String): UserProfile? = userProfileJpa.findByName(username).orElse(null)?.toUserProfile()

    @Transactional(readOnly = true)
    override fun findById(id: Long): UserProfile? = userProfileJpa.findById(id).orElse(null)?.toUserProfile()
}
