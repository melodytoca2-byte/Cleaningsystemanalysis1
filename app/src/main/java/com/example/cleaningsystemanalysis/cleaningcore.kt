
package com.example.cleaningsystemanalysis

class CleaningCore {


    fun completeZone(employee: Employee, zone: WorkZone, client: Client, actualTimeSec: Long) {

        val minExpectedTime = zone.area * 5

        if (actualTimeSec < minExpectedTime) {
            zone.status = "BLOCKED"

            employee.workHistory.add(WorkRecord(zone.id, zone.area, 0.0, isVerified = false))
            println("ALERT: Обнаружена ошибка в зоне ${zone.id}. Заблокировано.")
            return
        }


        val payment = zone.area * zone.difficulty * client.baseRate
        employee.dailyBalance += payment

        val record = WorkRecord(
            zoneId = zone.id,
            area = zone.area,
            moneyEarned = payment
        )
        employee.workHistory.add(record)

        zone.status = "DONE"
        zone.workerId = employee.id
        println("Система: Сотруднику ${employee.fullName} начислено $payment за зону ${zone.id}")
    }


    fun transferZone(zone: WorkZone, oldWorker: Employee, newWorker: Employee) {
        zone.workerId = newWorker.id
        zone.status = "BUSY"
        println("Смена зон: Зона ${zone.id} передана ${newWorker.fullName}")
    }


    fun getSmartRoute(zones: List<WorkZone>): List<WorkZone> {
        return zones.filter { it.status == "FREE" }
            .sortedByDescending { it.priority }
    }

    fun generateFinalReport(employee: Employee): String {
        val sb = StringBuilder()
        sb.append("ОТЧЕТ ПО СМЕНЕ: ${employee.fullName}\n")
        sb.append("--------------------------------\n")
        employee.workHistory.forEach {
            val statusIcon = if (it.isVerified) "✅" else "❌"
            sb.append("Зона: ${it.zoneId} | ${it.moneyEarned} тг | $statusIcon\n")
        }
        sb.append("--------------------------------\n")
        sb.append("ИТОГО К ВЫПЛАТЕ: ${employee.dailyBalance} тг")
        return sb.toString()
    }
}