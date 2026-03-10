package com.kou.usagiappapi.tool

import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.YearMonth

// LocalDate로부터 해당 월의 마지막 날짜 구하기
// val date = LocalDate.now()
// val lastDayFromDate = date.withDayOfMonth(date.lengthOfMonth())
// println(lastDayFromDate)

@Configuration
class DateTool {
    companion object {
        fun getFirstDayOfCurrentMonth(): LocalDate = YearMonth.now().atDay(1)

        fun getLastDayOfCurrentMonth(): LocalDate = YearMonth.now().atEndOfMonth()

        fun getLastDayOfMonth(yearMonth: YearMonth): LocalDate = yearMonth.atEndOfMonth()
    }
}
