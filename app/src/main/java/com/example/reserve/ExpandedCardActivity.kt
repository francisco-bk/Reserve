package com.example.reserve

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpandedCardActivity : AppCompatActivity() {

    private lateinit var backButton: FloatingActionButton
    private lateinit var reserveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_card)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // TODO: add putExtras
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            val intent = Intent(this, RequestReservationActivity::class.java).apply {
                // TODO: add putExtras
            }
            startActivity(intent)
        }

    }

}