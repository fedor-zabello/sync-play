package org.pigletsinc.syncplay.repository.user.jpainterfaces

import org.pigletsinc.syncplay.repository.user.entity.GoogleOauthJpa
import org.springframework.data.jpa.repository.JpaRepository

interface GoogleOauthRepositoryJpa : JpaRepository<GoogleOauthJpa, Long> {
    fun findByEmailIgnoreCase(email: String): GoogleOauthJpa?
    fun findByOauthId(oauthId: String): GoogleOauthJpa?
}
