package com.example.cleaningsystemanalysis


data class WorkZone(
    val id: String,
    val area: Double,
    val difficulty: Double,

    var isOccupied: Boolean = false,


    var workerId: String? = null



data class Employee(
    val id: String,
    val name: String,
    var currentBalance: Double = 0.0 .
)