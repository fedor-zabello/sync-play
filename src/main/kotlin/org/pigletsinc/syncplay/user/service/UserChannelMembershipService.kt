package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.repository.GoogleOauthRepository
import org.pigletsinc.syncplay.user.repository.UserCredentialsRepository
import org.springframework.stereotype.Service

@Service
class UserChannelMembershipService(
    googleOauthRepository: GoogleOauthRepository,
    userCredentialsRepository: UserCredentialsRepository
) {

    fun getChannelIdsByUserId(email: String? = null, googleId: String? = null): List<Long> {
        return emptyList()
    }

}