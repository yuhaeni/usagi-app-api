package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.Couple
import org.springframework.data.jpa.repository.JpaRepository

interface CoupleRepository : JpaRepository<Couple, Long>
