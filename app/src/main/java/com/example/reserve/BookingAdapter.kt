package com.example.reserve

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.Toast

import android.content.DialogInterface
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class BookingAdapter(private var rooms: List<Room>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>()  {

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val roomListType = Types.newParameterizedType(List::class.java, Room::class.java)
    private val roomListJsonAdapter : JsonAdapter<List<Room>> = moshi.adapter(roomListType)
    private val reservationJsonAdapter : JsonAdapter<Reservation> = moshi.adapter(Reservation::class.java)

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.hallTitle)
        val image: ImageView = itemView.findViewById(R.id.hallImage)
        val time: TextView = itemView.findViewById(R.id.roomTime)
        val dow: TextView = itemView.findViewById(R.id.dow)
        val date: TextView = itemView.findViewById(R.id.date)
        val cancel: Button = itemView.findViewById(R.id.cancel)
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

        holder.cancel.setOnClickListener{

            AlertDialog.Builder(context)
                .setTitle("Are you sure you want to cancel your booking?")
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { _, _ ->
                        Repository.reservedRooms.remove(room)
                        val intent = Intent(context, MainActivity::class.java).apply {
                        }
                        context.startActivity(intent)
                    })
                .setNegativeButton(android.R.string.no, null).show()
        }
    }

    override fun getItemCount(): Int {
        return rooms.size;
    }

}