package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    var email: String,
    var password: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)