package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class User(
    var email: String,
    var password: String,
    @Id @GeneratedValue var id: Long?
)