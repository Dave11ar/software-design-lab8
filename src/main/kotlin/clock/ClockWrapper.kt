package clock

import java.time.Clock
import java.time.Duration
import java.time.ZoneId
import java.util.Date

class ClockWrapper(private var clock: Clock = Clock.fixed(Date().toInstant(), ZoneId.systemDefault())) {
    fun tick(duration: Duration) {
        clock = Clock.offset(clock, duration)
    }

    fun get() = clock
}