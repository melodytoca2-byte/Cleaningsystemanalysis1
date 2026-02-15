package com.example.cleaningsystemanalysis

class CleaningCore {
    // Список зон теперь хранится здесь, а не в Activity
    private val zones = mutableListOf(
        WorkZone("101", 15.0, 1.0, priority = 5),
        WorkZone("102", 20.0, 1.2, priority = 3),
        WorkZone("103", 10.0, 1.5, priority = 1),
        WorkZone("104", 30.0, 1.1, priority = 4)
    )

    fun getAvailableZones(): List<WorkZone> = zones

    fun calculateTimeForZone(id: String): Int {
        // Здесь потом сделаешь реальный таймер, пока просто логика
        return (100..600).random()
    }

    fun completeZone(worker: Employee, zone: WorkZone, client: Client, timeSec: Int) {
        zone.status = "DONE"
        worker.dailyBalance += (zone.area * client.baseRate).toInt()
    }

    fun generateFinalReport(worker: Employee): String {
        return "Отчет для: ${worker.fullName}\nЗаработано: ${worker.dailyBalance} тг"
    }
}