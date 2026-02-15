package com.example.cleaningsystemanalysis

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val core = CleaningCore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentClient = Client("c_001", "БЦ Глобус", 500.0)
        val myAccount = Employee("e_77", "Асет Ибраев")
        val currentZone = WorkZone("zone_10", 25.0, 1.2)

        val txtBalance = findViewById<TextView>(R.id.resultPrice)
        val btnFinish = findViewById<Button>(R.id.btnCalculate)

        btnFinish.setOnClickListener {
            core.completeZone(myAccount, currentZone, currentClient, 600) // 600 сек работы
            txtBalance.text = "Баланс: ${myAccount.dailyBalance} тг"
            Toast.makeText(this, "Зона готова!", Toast.LENGTH_SHORT).show()
        }


        val btnTransfer = findViewById<Button>(R.id.btnTransfer)
        btnTransfer?.setOnClickListener {
            val otherWorker = Employee("e_99", "Сменный Работник")
            core.transferZone(currentZone, myAccount, otherWorker)
            Toast.makeText(this, "Зона передана", Toast.LENGTH_SHORT).show()
        }
    }
}