package com.example.cleaningsystemanalysis

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private val core = CleaningCore()


    private val myAccount = Employee("e_77", "Асет Ибраев")
    private val currentClient = Client("c_001", "БЦ Глобус", 550.0)


    private val allZones = listOf(
        WorkZone("101", 15.0, 1.0, priority = 5),
        WorkZone("102", 20.0, 1.2, priority = 3),
        WorkZone("103", 10.0, 1.5, priority = 1),
        WorkZone("104", 30.0, 1.1, priority = 4)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateHeader()
        renderMap()


        val btnReport = findViewById<Button>(R.id.btnCalculate)
        btnReport.text = "Сформировать отчет"
        btnReport.setOnClickListener {
            showFinalReport()
        }
    }


    private fun renderMap() {
        val grid = findViewById<GridLayout>(R.id.mapGrid)
        grid.removeAllViews()


        val smartRoute = core.getSmartRoute(allZones)

        smartRoute.forEach { zone ->
            val zoneButton = Button(this).apply {
                text = "Зона ${zone.id}\n${zone.area}м²"
                setPadding(10, 10, 10, 10)


                updateZoneStyle(this, zone)

                setOnClickListener {

                    core.completeZone(myAccount, zone, currentClient, 300)
                    updateZoneStyle(this, zone)
                    updateHeader()
                    Toast.makeText(context, "Зона ${zone.id} убрана", Toast.LENGTH_SHORT).show()
                }

                setOnLongClickListener {

                    val other = Employee("e_99", "Сменщик")
                    core.transferZone(zone, myAccount, other)
                    updateZoneStyle(this, zone)
                    true
                }
            }
            grid.addView(zoneButton)
        }
    }


    private fun updateHeader() {
        findViewById<TextView>(R.id.resultPrice).text = "${myAccount.dailyBalance} тг"
        findViewById<TextView>(R.id.workerInfo).text = "Сотрудник: ${myAccount.fullName} | ★ ${myAccount.rating}"
    }


    private fun updateZoneStyle(btn: Button, zone: WorkZone) {
        when(zone.status) {
            "FREE" -> btn.setBackgroundColor(Color.LTGRAY)
            "BUSY" -> btn.setBackgroundColor(Color.YELLOW)
            "DONE" -> {
                btn.setBackgroundColor(Color.GREEN)
                btn.isEnabled = false
            }
            "BLOCKED" -> btn.setBackgroundColor(Color.RED)
        }
    }


    private fun showFinalReport() {
        val reportText = core.generateFinalReport(myAccount)
        AlertDialog.Builder(this)
            .setTitle("Бланк выполненных работ")
            .setMessage(reportText)
            .setPositiveButton("OK", null)
            .show()
    }
}