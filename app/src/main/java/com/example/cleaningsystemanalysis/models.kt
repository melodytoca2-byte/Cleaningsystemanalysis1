package com.example.cleaningsystemanalysis

enum class UserRole { WORKER }

data class UserProfile(
    val id: String,
    val name: String,
    val role: UserRole,
    var balance: Double = 0.0
)

data class WorkZone(
    val id: String,
    val area: Double,
    var status: String = "FREE",
    var workerId: String? = null,
    val priority: Int = 1,
    val difficulty: Double = 1.0,
    val row: Int,
    val col: Int
)

data class WorkLog(val timestamp: String, val message: String)