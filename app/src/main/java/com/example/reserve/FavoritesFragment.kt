package com.example.reserve

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noFav : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_favorites, container, false)
        val roomList = mutableListOf<Room>()

        recyclerView = view.findViewById(R.id.bookings)
        noFav = view.findViewById(R.id.noFav)

        if (Repository.favorites.size == 0) {
            noFav.text = "You don't have any favorites. \nIf you favorite any rooms, they will show up here \uD83D\uDE03"
        }
        else {
            noFav.text = ""

            for (favorite in Repository.favorites) {
                roomList.add(favorite)
                Log.d("xyz", favorite.room)
            }

        }

        val adapter = Adapter(roomList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoritesFragment().apply {
            }
    }
}