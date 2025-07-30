package com.goofy.realtime.inbound.mysql.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

sealed interface BaseEntity {
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener::class)
    abstract class AppendOnly : BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val _id: Long? = null

        val id: Long
            get() = checkNotNull(_id) { "저장되지 않은 엔티티의 id 조회 입니다." }

        @Column(name = "created_at", updatable = false)
        @CreatedDate
        lateinit var createdAt: LocalDateTime
    }

    @MappedSuperclass
    @EntityListeners(AuditingEntityListener::class)
    abstract class Update : BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val _id: Long? = null

        val id: Long
            get() = checkNotNull(_id) { "저장되지 않은 엔티티의 id 조회 입니다." }

        @Column(name = "created_at", updatable = false)
        @CreatedDate
        lateinit var createdAt: LocalDateTime

        @Column(name = "updated_at", updatable = false)
        @LastModifiedDate
        lateinit var updatedAt: LocalDateTime
    }

    @MappedSuperclass
    @EntityListeners(AuditingEntityListener::class)
    abstract class UpdateWithoutId : BaseEntity {
        @Column(name = "created_at", updatable = false)
        @CreatedDate
        lateinit var createdAt: LocalDateTime

        @Column(name = "updated_at", updatable = false)
        @LastModifiedDate
        lateinit var updatedAt: LocalDateTime
    }
}
