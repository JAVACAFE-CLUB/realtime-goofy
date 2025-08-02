package com.goofy.realtime.outbound.mysql.repository

import com.goofy.realtime.outbound.mysql.entity.Trend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrendJpaRepository : JpaRepository<Trend, Long>
