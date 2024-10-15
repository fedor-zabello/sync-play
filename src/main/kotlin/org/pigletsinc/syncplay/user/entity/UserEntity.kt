package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    var email: String,
    var password: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
)
