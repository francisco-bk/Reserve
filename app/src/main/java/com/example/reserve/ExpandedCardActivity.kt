package com.example.reserve

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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

        val building = intent.extras?.getString("building")
        val room = intent.extras?.getString("room")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room


        val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(), "", "Jan 1, 2021", "Saturday")

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            val intent = Intent(this, RequestReservationActivity::class.java).apply {
            }
            startActivity(intent)
        }

        // temporary favorite implementation stored locally for testing
        var isFavorite = false
        for (favorite in Repository.favorites) {
            if (favorite.room == roomName.text.toString()) {
                isFavorite = true
            }
        }

        if (isFavorite) {
            favoriteButtonInactivated.hide()
            favoriteButtonActivated.show()
        } else {
            favoriteButtonActivated.hide()
            favoriteButtonInactivated.show()
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
            Log.d("FAVORITES", Repository.favorites.toString())
        }

    }

}