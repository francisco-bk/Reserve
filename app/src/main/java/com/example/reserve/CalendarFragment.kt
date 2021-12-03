package com.example.reserve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarFragment : Fragment() {

    private lateinit var recyclerView2: RecyclerView
    private lateinit var noBook : TextView
    private lateinit var noBookDesc : TextView
    private lateinit var bookImage : ImageView
    private lateinit var month: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)

        recyclerView2 = view.findViewById(R.id.bookings2)
        noBook = view.findViewById(R.id.noBook)
        noBookDesc = view.findViewById(R.id.noBookDesc)
        bookImage = view.findViewById(R.id.noBookImage)
        month = view.findViewById(R.id.textView2)

        val roomList = mutableListOf<Room>()

        if (Repository.reservedRooms.size == 0) {
            noBook.text = "No bookings yet"
            noBookDesc.text = "When you book a room, you reservation will show up here"
            month.text = ""
            bookImage.setImageResource(R.drawable.no_booking)
        }
        else {
            noBook.text = ""
            noBookDesc.text = ""
            month.text = "December"
            bookImage.setImageResource(0)

            for (booking in Repository.reservedRooms) {
                roomList.add(booking)
            }

        }

        val adapter = BookingAdapter(roomList)
        recyclerView2.adapter = adapter
        recyclerView2.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CalendarFragment().apply {

            }
    }
}