package com.goofy.realtime.inbound.mysql.repository

import com.goofy.realtime.inbound.mysql.entity.Trend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrendJpaRepository : JpaRepository<Trend, Long>
