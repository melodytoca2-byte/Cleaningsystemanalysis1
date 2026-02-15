package com.example.cleaningsystemanalysis
import com.example.cleaningsystemanalysis.R
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
        setContentView(R.layout.interfacecsa)

        updateHeader()
        renderMap()

        val btnReport = findViewById<Button>(R.id.btnCalculate)
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
            }
            grid.addView(zoneButton)
        }
    }

    private fun updateHeader() {
        val txtPrice = findViewById<TextView>(R.id.resultPrice)
        val txtInfo = findViewById<TextView>(R.id.workerInfo)

        txtPrice.text = "${myAccount.dailyBalance} тг"
        txtInfo.text = getString(R.string.worker_label, myAccount.fullName, myAccount.rating)
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
            .setTitle(getString(R.string.report_title))
            .setMessage(reportText)
            .setPositiveButton("OK", null)
            .show()
    }
}