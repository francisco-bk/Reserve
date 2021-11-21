package com.example.reserve

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noFav : TextView
    private lateinit var noFavDesc : TextView
    private lateinit var image : ImageView


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
        noFavDesc = view.findViewById(R.id.noFavDesc)
        image = view.findViewById(R.id.noFavImage)

        if (Repository.favorites.size == 0) {
            noFav.text = "No favorites yet"
            noFavDesc.text = "Tap the star icon on your favorite rooms and they'll show up here"
            image.setImageResource(R.drawable.no_favs)
        }
        else {
            noFav.text = ""
            noFavDesc.text = ""
            image.setImageResource(0)

            for (favorite in Repository.favorites) {
                roomList.add(favorite)
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