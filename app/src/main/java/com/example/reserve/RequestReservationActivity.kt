package com.example.reserve

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton

class RequestReservationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_reservation)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        checkBox = findViewById(R.id.checkBox)

        reserveButton.isEnabled = false
        reserveButton.setBackgroundColor(Color.GRAY)

        backButton.setOnClickListener {
            val intent = Intent(this, ExpandedCardActivity::class.java).apply {
                // TODO: add putExtras
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // TODO: add putExtras
            }
            startActivity(intent)
        }

        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                reserveButton.isEnabled = true
                reserveButton.setBackgroundColor(Color.RED)
            } else {
                reserveButton.isEnabled = false
                reserveButton.setBackgroundColor(Color.GRAY)
            }
        }
    }
}