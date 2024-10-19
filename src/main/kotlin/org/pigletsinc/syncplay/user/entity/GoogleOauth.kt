package org.pigletsinc.syncplay.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "google_oauth")
data class GoogleOauth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val oauthId: String,
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    val userProfile: UserProfile,
)
