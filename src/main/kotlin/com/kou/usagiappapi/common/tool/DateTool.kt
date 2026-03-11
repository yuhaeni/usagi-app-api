package com.kou.usagiappapi.common.tool

import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.YearMonth

@Configuration
class DateTool {
    companion object {
        fun getFirstDayOfCurrentMonth(): LocalDate = YearMonth.now().atDay(1)

        fun getLastDayOfCurrentMonth(): LocalDate = YearMonth.now().atEndOfMonth()

        fun getLastDayOfMonth(yearMonth: YearMonth): LocalDate = yearMonth.atEndOfMonth()
    }
}
