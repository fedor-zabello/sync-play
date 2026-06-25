package org.pigletsinc.syncplay.domain.user.repository

import org.pigletsinc.syncplay.domain.user.entity.GoogleOauth

interface IGoogleOauthRepository {
    fun save(googleOauth: GoogleOauth): GoogleOauth
    fun findByEmailIgnoreCase(email: String): GoogleOauth?
    fun findByOauthId(oauthId: String): GoogleOauth?
}
