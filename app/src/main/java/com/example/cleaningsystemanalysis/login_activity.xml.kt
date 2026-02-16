package com.example.cleaningsystemanalysis

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val staff = listOf("Иван Иванов", "Мария Сидорова", "Дмитрий Петров")
        val spinner = findViewById<Spinner>(R.id.userSpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, staff)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val name = spinner.selectedItem.toString()
            val i = Intent(this, MainActivity::class.java).apply {
                putExtra("USER_NAME", name)
            }
            startActivity(i)
        }
    }
}