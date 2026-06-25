package org.pigletsinc.syncplay.repository.user.impl

import org.pigletsinc.syncplay.domain.user.entity.GoogleOauth
import org.pigletsinc.syncplay.domain.user.repository.IGoogleOauthRepository
import org.pigletsinc.syncplay.repository.user.jpainterfaces.GoogleOauthRepositoryJpa
import org.pigletsinc.syncplay.repository.user.jpainterfaces.UserProfileRepositoryJpa
import org.pigletsinc.syncplay.repository.user.mapper.toGoogleOauth
import org.pigletsinc.syncplay.repository.user.mapper.toJpa
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class GoogleOauthRepositoryImpl(
    private val googleOauthJpa: GoogleOauthRepositoryJpa,
    private val userProfileJpa: UserProfileRepositoryJpa,
) : IGoogleOauthRepository {
    override fun save(googleOauth: GoogleOauth): GoogleOauth {
        val userProfileJpaEntity = userProfileJpa.getReferenceById(googleOauth.userProfile.id!!)
        val jpa = googleOauth.toJpa(userProfileJpaEntity)
        return googleOauthJpa.save(jpa).toGoogleOauth()
    }

    @Transactional(readOnly = true)
    override fun findByEmailIgnoreCase(email: String): GoogleOauth? =
        googleOauthJpa.findByEmailIgnoreCase(email)?.toGoogleOauth()

    @Transactional(readOnly = true)
    override fun findByOauthId(oauthId: String): GoogleOauth? = googleOauthJpa.findByOauthId(oauthId)?.toGoogleOauth()
}
