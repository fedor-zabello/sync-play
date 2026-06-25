package org.pigletsinc.syncplay.repository.user.mapper

import org.pigletsinc.syncplay.domain.user.entity.GoogleOauth
import org.pigletsinc.syncplay.domain.user.entity.UserCredentials
import org.pigletsinc.syncplay.domain.user.entity.UserProfile
import org.pigletsinc.syncplay.repository.channel.mapper.toChannel
import org.pigletsinc.syncplay.repository.user.entity.GoogleOauthJpa
import org.pigletsinc.syncplay.repository.user.entity.UserCredentialsJpa
import org.pigletsinc.syncplay.repository.user.entity.UserProfileJpa

fun UserProfileJpa.toUserProfile(): UserProfile =
    UserProfile(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
        channels = this.channels.map { it.toChannel() }.toSet(),
    )

fun UserProfile.toJpa(): UserProfileJpa =
    UserProfileJpa(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
    )

fun UserCredentialsJpa.toUserCredentials(): UserCredentials =
    UserCredentials(
        id = this.id,
        email = this.email,
        password = this.password,
        userProfile = this.userProfile.toUserProfile(),
    )

fun UserCredentials.toJpa(userProfileJpa: UserProfileJpa): UserCredentialsJpa =
    UserCredentialsJpa(
        id = this.id,
        email = this.email,
        password = this.password,
        userProfile = userProfileJpa,
    )

fun GoogleOauthJpa.toGoogleOauth(): GoogleOauth =
    GoogleOauth(
        id = this.id,
        oauthId = this.oauthId,
        email = this.email,
        userProfile = this.userProfile.toUserProfile(),
    )

fun GoogleOauth.toJpa(userProfileJpa: UserProfileJpa): GoogleOauthJpa =
    GoogleOauthJpa(
        id = this.id,
        oauthId = this.oauthId,
        email = this.email,
        userProfile = userProfileJpa,
    )
