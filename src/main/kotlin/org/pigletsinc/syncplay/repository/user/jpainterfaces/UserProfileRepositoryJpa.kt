package org.pigletsinc.syncplay.repository.user.jpainterfaces

import org.pigletsinc.syncplay.repository.user.entity.UserProfileJpa
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserProfileRepositoryJpa : JpaRepository<UserProfileJpa, Long> {
    fun findByName(username: String): Optional<UserProfileJpa>
}
