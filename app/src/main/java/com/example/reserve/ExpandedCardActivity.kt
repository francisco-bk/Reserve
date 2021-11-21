package com.example.reserve

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpandedCardActivity : AppCompatActivity() {

    private lateinit var backButton: FloatingActionButton
    private lateinit var reserveButton: Button
    private lateinit var favoriteButtonActivated: FloatingActionButton
    private lateinit var favoriteButtonInactivated: FloatingActionButton

    private lateinit var roomName: TextView
    private lateinit var buildingName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_card)

        roomName = findViewById(R.id.roomName)
        buildingName = findViewById(R.id.buildingName)
        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        favoriteButtonActivated = findViewById(R.id.favoriteButtonActivated)
        favoriteButtonInactivated = findViewById(R.id.favoriteButtonInactivated)

        val building = getIntent().extras?.getString("building")
        val room = getIntent().extras?.getString("room")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room

        val tempTimes : Array<Boolean> = arrayOf(true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true)
        val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(), tempTimes, "Jan 1, 2021")

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

        // temporary favorite implementation stored locally for testing
        var isFavorite = false
        for (favorite in Repository.favorites) {
            if (favorite.room.equals(roomName.text.toString())) {
                isFavorite = true
            }
        }

        favoriteButtonActivated.setOnClickListener {
            isFavorite = false
            favoriteButtonActivated.hide()
            favoriteButtonInactivated.show()
            Repository.favorites.remove(roomObj)
            Log.d("FAVORITES", "removed")
            Log.d("FAVORITES", Repository.favorites.toString())
        }

        favoriteButtonInactivated.setOnClickListener {
            isFavorite = true
            favoriteButtonInactivated.hide()
            favoriteButtonActivated.show()
            Repository.favorites.add(roomObj)
            Log.d("FAVORITES", "added")
        }

    }

}