package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_profile")
class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var name: String,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @ManyToMany
    @JoinTable(
        name = "user_profile_channel",
        joinColumns = [JoinColumn(name = "user_profile_id")],
        inverseJoinColumns = [JoinColumn(name = "channel_id")],
    )
    var channels: MutableSet<Channel> = mutableSetOf(),
)
