package com.example.cleaningsystemanalysis

data class Client(
    val id: String,
    val name: String,
    val baseRate: Double
)


data class WorkZone(
    val id: String,
    val area: Double,
    val difficulty: Double,
    var status: String = "FREE",
    var workerId: String? = null,
    val row: Int = 0,
    val col: Int = 0,

    var priority: Int = 1,
    var mapX: Float = 0f,
    var mapY: Float = 0f
)


data class Employee(
    val id: String,
    val fullName: String,
    var dailyBalance: Double = 0.0,
    var rating: Double = 5.0,
    val workHistory: MutableList<WorkRecord> = mutableListOf()
)


data class WorkRecord(
    val zoneId: String,
    val area: Double,
    val moneyEarned: Double,
    val startTime: Long = System.currentTimeMillis(),
    val timestamp: Long = System.currentTimeMillis(),
    var isVerified: Boolean = true
)