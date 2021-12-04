package com.example.reserve

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
        holder.text.text = room.room_name
        Glide.with(context).load(room.image).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ExpandedCardActivity::class.java).apply {
                putExtra("id", room.id)
                putExtra("location", room.location)
                putExtra("building", room.hall)
                putExtra("room_name", room.room_name)
                putExtra("features", room.features)
                putExtra("capacity", room.capacity)
                putExtra("image", room.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

}