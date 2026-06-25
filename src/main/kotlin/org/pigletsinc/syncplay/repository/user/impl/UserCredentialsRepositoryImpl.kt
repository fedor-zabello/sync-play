package org.pigletsinc.syncplay.repository.user.impl

import org.pigletsinc.syncplay.domain.user.entity.UserCredentials
import org.pigletsinc.syncplay.domain.user.repository.IUserCredentialsRepository
import org.pigletsinc.syncplay.repository.user.jpainterfaces.UserCredentialsRepositoryJpa
import org.pigletsinc.syncplay.repository.user.jpainterfaces.UserProfileRepositoryJpa
import org.pigletsinc.syncplay.repository.user.mapper.toJpa
import org.pigletsinc.syncplay.repository.user.mapper.toUserCredentials
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserCredentialsRepositoryImpl(
    private val userCredentialsJpa: UserCredentialsRepositoryJpa,
    private val userProfileJpa: UserProfileRepositoryJpa,
) : IUserCredentialsRepository {
    override fun save(userCredentials: UserCredentials): UserCredentials {
        val userProfileJpaEntity = userProfileJpa.getReferenceById(userCredentials.userProfile.id!!)
        val jpa = userCredentials.toJpa(userProfileJpaEntity)
        return userCredentialsJpa.save(jpa).toUserCredentials()
    }

    @Transactional(readOnly = true)
    override fun findByEmailIgnoreCase(email: String): UserCredentials? =
        userCredentialsJpa.findByEmailIgnoreCase(email)?.toUserCredentials()
}
