package miragefairy2019.libkt

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

// UTC変換
fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)
fun LocalDateTime.toInstant(): Instant = toInstant(ZoneOffset.UTC)

operator fun Instant.minus(other: Instant): Duration = Duration.ofMillis(toEpochMilli() - other.toEpochMilli())

fun LocalDate.withZeroTime(): LocalDateTime = atTime(0, 0, 0)

// TODO rename atStartOfDay
// TODO Instantに対して直接実行できるように
// TODO remove atDayStartは既存メソッドがある
fun LocalDate.atDayStart() = withZeroTime()
fun LocalDate.atWeekStart() = minusDays((dayOfWeek.value % 7).toLong()).withZeroTime()
fun LocalDate.atMonthStart() = withDayOfMonth(1).withZeroTime()
