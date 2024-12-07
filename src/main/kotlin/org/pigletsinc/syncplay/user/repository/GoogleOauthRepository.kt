package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.GoogleOauth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoogleOauthRepository : JpaRepository<GoogleOauth, Long> {
    fun findByEmailIgnoreCase(email: String): GoogleOauth?
    fun findByOauthId(oauthId: String): GoogleOauth?
}
