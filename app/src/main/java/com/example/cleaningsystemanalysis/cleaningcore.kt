package com.example.cleaningsystemanalysis

import java.text.SimpleDateFormat
import java.util.*

class CleaningCore {
    private val zones = mutableListOf<WorkZone>()
    val history = mutableListOf<WorkLog>()
    private var updateCallback: (() -> Unit)? = null

    init {
        // Создаем карту зон
        zones.add(WorkZone("101", 15.0, "FREE", null, 10, 1.2, 0, 0))
        zones.add(WorkZone("102", 25.0, "FREE", null, 5, 1.5, 0, 1))
        zones.add(WorkZone("103", 10.0, "FREE", null, 8, 1.0, 1, 0))
        zones.add(WorkZone("104", 40.0, "FREE", null, 2, 2.0, 1, 1))
    }

    fun setOnDataChangedListener(callback: () -> Unit) {
        this.updateCallback = callback
    }

    fun getAvailableZones() = zones

    fun getNextTarget(): WorkZone? {
        return zones.filter { it.status == "FREE" }.maxByOrNull { it.priority }
    }

    fun getProgress(): Int {
        if (zones.isEmpty()) return 0
        return (zones.count { it.status == "DONE" } * 100) / zones.size
    }

    fun processAction(user: UserProfile, zone: WorkZone) {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        when (zone.status) {
            "FREE" -> {
                zone.status = "IN_PROGRESS"
                zone.workerId = user.id
                history.add(0, WorkLog(time, "${user.name} начал работу в ${zone.id}"))
            }
            "IN_PROGRESS" -> {
                if (zone.workerId == user.id) {
                    val reward = zone.area * 550.0 * zone.difficulty
                    user.balance += reward
                    zone.status = "DONE"
                    history.add(0, WorkLog(time, "${user.name} выполнил ${zone.id} (+$reward тг)"))
                }
            }
        }
        updateCallback?.invoke()
    }
}