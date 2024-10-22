package org.pigletsinc.syncplay.channel.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_profile_channel")
data class UserChannelMembership(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_profile_id", nullable = false)
    val userProfileId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    val channel: Channel
)
