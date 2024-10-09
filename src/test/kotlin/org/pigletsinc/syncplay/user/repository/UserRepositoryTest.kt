package org.pigletsinc.syncplay.user.repository

import org.assertj.core.api.Assertions.assertThat
import org.pigletsinc.syncplay.user.entity.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test

@DataJpaTest(excludeAutoConfiguration = [FlywayAutoConfiguration::class])
class UserRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository
) {

    @Test
    fun `When findByEmail then return User`() {
        val mrPiglet = UserEntity("pig@let.com", "pass")
        entityManager.persist(mrPiglet)
        entityManager.flush()
        val foundUser = userRepository.findByEmail("pig@let.com")
        assertThat(foundUser).isEqualTo(mrPiglet)
    }
}