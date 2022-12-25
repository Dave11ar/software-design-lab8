package statistics

interface EventsStatistics {
    fun incEvent(name: String)
    fun getEventStatistic(name: String): Double
    fun getAllEventStatistic(): Map<String, Double>
}