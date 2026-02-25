package org.pigletsinc.syncplay.domain.user.entity

data class GoogleOauth(
    val id: Long? = null,
    val oauthId: String,
    val email: String,
    val userProfile: UserProfile,
)
