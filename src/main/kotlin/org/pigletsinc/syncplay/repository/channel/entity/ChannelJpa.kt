package org.pigletsinc.syncplay.repository.channel.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.pigletsinc.syncplay.repository.user.entity.UserProfileJpa

@Entity
@Table(name = "channel")
class ChannelJpa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    @ManyToMany(mappedBy = "channels")
    var userProfiles: MutableSet<UserProfileJpa> = mutableSetOf(),
)
