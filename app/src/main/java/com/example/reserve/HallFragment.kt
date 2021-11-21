package com.example.reserve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class HallFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val roomList = mutableListOf<Room>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hall, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        // temp data being added, waiting for backend
        for(i in 1..40) {
            roomList.add(Room("West", "UPS" + (i+200).toString(), "Upson", "", "11/20/2021", "Saturday"))
        }

        val adapter = Adapter(roomList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HallFragment().apply {

            }
    }
}