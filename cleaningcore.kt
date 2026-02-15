package com.example.cleaningsystemanalysis

class CleaningCore {


    fun calculatePayment(zone: WorkZone, baseRate: Double): Double {
        val result = zone.area * zone.difficulty * baseRate
        return result
    }


    fun reassignZone(zone: WorkZone, newWorker: Employee) {

        zone.isOccupied = true
        println("LOG: Зона ${zone.id} перехвачена сотрудником ${newWorker.name}")
    }
}