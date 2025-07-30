package com.goofy.realtime.inbound.mysql.entity

import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.inbound.mysql.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "trend")
class Trend(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "seq")
    var seq: Int,
) : BaseEntity.UpdateWithoutId() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val _id: Long? = null

    val id: TrendId
        get() = TrendId(checkNotNull(_id) { "저장되지 않은 엔티티의 id 조회 입니다." })
}
