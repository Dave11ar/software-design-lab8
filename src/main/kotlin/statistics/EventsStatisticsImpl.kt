package statistics

import clock.ClockWrapper
import java.time.Instant
import java.time.temporal.ChronoUnit

class EventsStatisticsImpl(private val clockWrapper: ClockWrapper = ClockWrapper()) : EventsStatistics{
    private val events: MutableMap<String, MutableList<Instant>> = mutableMapOf()

    override fun incEvent(name: String) {
        events.putIfAbsent(name, mutableListOf())
        events[name]!!.add(clockWrapper.get().instant())
    }

    override fun getEventStatistic(name: String): Double =
        clockWrapper.get().instant().minus(1, ChronoUnit.HOURS).let { hourAgo ->
            (events[name]?.filter { it > hourAgo }?.size?.toDouble() ?: 0.0) / 60.0
        }

    override fun getAllEventStatistic(): Map<String, Double> = events.mapValues { getEventStatistic(it.key) }
}