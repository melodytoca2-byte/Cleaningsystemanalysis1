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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interfacecsa)

        updateHeader()
        renderMap()

        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            showFinalReport()
        }
    }

    private fun renderMap() {
        val grid = findViewById<GridLayout>(R.id.mapGrid)
        grid.removeAllViews()

        core.getAvailableZones().forEach { zone ->
            val zoneButton = Button(this).apply {
                text = "Зона ${zone.id}\n${zone.area}м²"
                updateZoneStyle(this, zone)

                setOnClickListener {
                    val timeSpent = core.calculateTimeForZone(zone.id)
                    core.completeZone(myAccount, zone, currentClient, timeSpent)
                    updateZoneStyle(this, zone)
                    updateHeader()
                    Toast.makeText(context, "Зона ${zone.id} готова!", Toast.LENGTH_SHORT).show()
                }
            }
            grid.addView(zoneButton)
        }
    }

    private fun updateHeader() {
        findViewById<TextView>(R.id.resultPrice).text = "${myAccount.dailyBalance} тг"
        findViewById<TextView>(R.id.workerInfo).text =
            getString(R.string.worker_label, myAccount.fullName, myAccount.rating)
    }

    private fun updateZoneStyle(btn: Button, zone: WorkZone) {
        btn.setBackgroundColor(if (zone.status == "DONE") Color.GREEN else Color.LTGRAY)
        btn.isEnabled = zone.status != "DONE"
    }

    private fun showFinalReport() {
        val reportText = core.generateFinalReport(myAccount)
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.report_title))
            .setMessage(reportText)
            .setPositiveButton("OK", null)
            .show()
    }
}