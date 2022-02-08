@file:Suppress("unused")

package mirrg.kotlin

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDate.withZeroTime(): LocalDateTime = atTime(0, 0, 0)
operator fun Instant.minus(other: Instant): Duration = Duration.ofMillis(toEpochMilli() - other.toEpochMilli())


// 切り捨て

val LocalDate.startOfDay get() = withZeroTime()

/** 日曜日を起点にします */
val LocalDate.startOfWeek get() = minusDays((dayOfWeek.value % 7).toLong()).withZeroTime()
val LocalDate.startOfMonth get() = withDayOfMonth(1).withZeroTime()
val LocalDate.startOfYear get() = withDayOfYear(1).withZeroTime()


// UTC変換
val Instant.utcLocalDateTime: LocalDateTime get() = LocalDateTime.ofInstant(this, ZoneOffset.UTC)
val LocalDateTime.toInstantAsUtc: Instant get() = toInstant(ZoneOffset.UTC)
