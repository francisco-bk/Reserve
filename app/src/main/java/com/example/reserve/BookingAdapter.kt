package com.example.reserve

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookingAdapter(private var rooms: List<Room>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>()  {

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.hallTitle)
        val image: ImageView = itemView.findViewById(R.id.hallImage)
        val time: TextView = itemView.findViewById(R.id.roomTime)
        val dow: TextView = itemView.findViewById(R.id.dow)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_cell, parent, false) as View
        return BookingAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = rooms[position]
        val context = holder.itemView.context

        holder.title.text = room.hall + " " + room.room_name.filter { it.isDigit() }
        holder.time.text = room.time
        Glide.with(context).load(room.image).into(holder.image)
        holder.dow.text = room.dow.uppercase()
        holder.date.text = room.date.dropLast(4).filter { it.isDigit() }

        holder.itemView.setOnClickListener {
            // onclick to cancel reservation page
        }
    }

    override fun getItemCount(): Int {
        return rooms.size;
    }

}