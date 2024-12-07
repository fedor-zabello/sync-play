package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.ChannelDto
import org.pigletsinc.syncplay.user.ChannelMapper
import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.springframework.stereotype.Service

@Service
class UserChannelMembershipService(
    private val googleOauthRepository: GoogleOauthRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val channelMapper: ChannelMapper,
) {
    fun getChannelIdsByUserId(email: String? = null, googleId: String? = null): List<ChannelDto> {
        val userProfile =
            email?.let { userCredentialsRepository.findByEmailIgnoreCase(email)?.userProfile }
                ?: googleId?.let { googleOauthRepository.findByOauthId(googleId)?.userProfile }
                ?: throw IllegalArgumentException("At least one user id should be provided")

        return userProfile.channels.map { channelMapper.toDto(it) }
    }
}
