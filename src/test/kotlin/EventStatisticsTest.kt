import clock.ClockWrapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import statistics.EventsStatisticsImpl
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventStatisticsTest {
    private var clockWrapper = ClockWrapper()
    private var eventsStatistics = EventsStatisticsImpl(clockWrapper)

    private fun init() {
        clockWrapper = ClockWrapper()
        eventsStatistics = EventsStatisticsImpl(clockWrapper)
    }

    private fun incEvents(events: Map<String, Int>) {
        events.forEach { event ->
            (0 until event.value).forEach { _ -> eventsStatistics.incEvent(event.key) }
        }
    }

    private fun setupEvents(events: Map<String, Int>) {
        init()

        incEvents(events) // not supposed to count
        clockWrapper.tick(Duration.ofHours(2))
        incEvents(events)
    }

    private fun gradualTimeIncrease(event: String, duration: Duration, times: Int) {
        init()
        (0 until times).forEach { _ ->
            clockWrapper.tick(duration)
            eventsStatistics.incEvent(event)
        }
    }

    @Test
    fun testSingleEvent() {
        setupEvents(mapOf(
            "test" to 10000
        ))

        Assertions.assertEquals(10000.0 / 60, eventsStatistics.getEventStatistic("test"))
    }

    @Test
    fun testSingleGradualTimeIncrease() {
        gradualTimeIncrease(
            "test",
            Duration.ofMinutes(1),
            10000
        )

        Assertions.assertEquals(1.0, eventsStatistics.getEventStatistic("test"))
    }

    @Test
    fun testMultipleEvents() {
        setupEvents(mapOf(
            "test0" to 10000,
            "test1" to 1000,
            "test2" to 100
        ))

        val allEventsStatistics = eventsStatistics.getAllEventStatistic()

        Assertions.assertEquals(allEventsStatistics["test0"], 10000.0 / 60)
        Assertions.assertEquals(allEventsStatistics["test1"], 1000.0 / 60)
        Assertions.assertEquals(allEventsStatistics["test2"], 100.0 / 60)
    }
}