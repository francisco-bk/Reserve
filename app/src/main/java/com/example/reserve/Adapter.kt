package com.example.reserve

import android.R.attr
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.R.attr.data

class Adapter(private var rooms: List<Room>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.hallTitle)
        val text: TextView = itemView.findViewById(R.id.roomText)
        val image: ImageView = itemView.findViewById(R.id.hallImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.room_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = rooms[position]
        val context = holder.itemView.context

        holder.title.text = room.hall
        holder.text.text = room.room
        holder.image.setImageResource(R.drawable.upson)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ExpandedCardActivity::class.java).apply {
                putExtra("building", room.hall)
                putExtra("room", room.room)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

}