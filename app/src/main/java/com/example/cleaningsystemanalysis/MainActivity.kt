package com.example.cleaningsystemanalysis

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val core = CleaningCore()
    private lateinit var user: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interfacecsa)


        val name = intent.getStringExtra("USER_NAME") ?: "Сотрудник"
        user = UserProfile("ID_${name.hashCode()}", name, UserRole.WORKER)


        core.setOnDataChangedListener { render() }
        render()
    }

    private fun render() {

        findViewById<TextView>(R.id.workerInfo).text = user.name
        findViewById<TextView>(R.id.resultPrice).text = "${user.balance} тг"

        val progress = core.getProgress()
        findViewById<ProgressBar>(R.id.progressBar).progress = progress
        findViewById<TextView>(R.id.progressText).text = "Выполнено: $progress%"

        val grid = findViewById<GridLayout>(R.id.mapGrid)
        grid.removeAllViews()

        val nextZone = core.getNextTarget()


        core.getAvailableZones().forEach { zone ->
            val zoneButton = Button(this).apply {
                text = "Зона ${zone.id}\n${zone.area}м²"

                layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(zone.row, 1f),
                    GridLayout.spec(zone.col, 1f)
                ).apply {
                    width = 0
                    height = 250
                    setMargins(8, 8, 8, 8)
                }


                when {
                    zone.status == "DONE" -> setBackgroundColor(Color.GREEN)
                    zone.status == "IN_PROGRESS" -> {
                        if (zone.workerId == user.id) setBackgroundColor(Color.YELLOW)
                        else setBackgroundColor(Color.RED)
                    }
                    zone == nextZone -> {
                        setBackgroundColor(Color.BLUE)
                        setTextColor(Color.WHITE)
                    }
                    else -> setBackgroundColor(Color.LTGRAY)
                }

                setOnClickListener {
                    showZoneDialog(zone)
                }
            }
            grid.addView(zoneButton)
        }


        val historyContainer = findViewById<LinearLayout>(R.id.historyLayout)
        historyContainer.removeAllViews()
        core.history.forEach { log ->
            val logEntry = TextView(this).apply {
                text = "[${log.timestamp}] ${log.message}"
                textSize = 12f
                setPadding(0, 4, 0, 4)
            }
            historyContainer.addView(logEntry)
        }
    }

    private fun showZoneDialog(zone: WorkZone) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Объект ${zone.id}")
        builder.setMessage("Площадь: ${zone.area} м²\nСтатус: ${zone.status}")

        if (zone.status != "DONE") {
            val actionText = if (zone.status == "FREE") "Взять в работу" else "Завершить очистку"
            builder.setPositiveButton(actionText) { _, _ ->
                core.processAction(user, zone)
            }
        }

        builder.setNegativeButton("Закрыть", null)
        builder.show()
    }
}